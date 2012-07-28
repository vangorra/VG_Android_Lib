package com.vg.lib.modules.applock;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vg.lib.module.Module;

/**
 * Facilitates locking and unlocking of an app based on inactivity or loss of focus.
 * This module can be configured with lockscreen activity to use for user presentation.
 * 
 * Example:
 * <pre>
 * public void onModuleLoad(ModuleManager moduleManager) {
 *   Bundle args = new Bundle();
 *   args.putString(
 *     AppLock.LOCKSCREEN_ACTIVITY_CLASS,
 *     com.yourcompany.activity.MyLockScreen.class.getName()
 *   ); 
 *   
 *   moduleManager.load(
 *     APP_LOCK_MOD_NAME,
 *     com.vg.lib.modules.AppLock.class,
 *     BundleBuilder.create()
 *       .putString(
 *         AppLock.LOCKSCREEN_ACTIVITY_CLASS,
 *         com.yourcompany.activity.MyLockScreen.class.getName()
 *       )
 *       .bundle()
 *   );
 * }
 * </pre>
 * 
 * @author vangorra
 *
 */
public class AppLockModule implements Module {
	/**
	 * Required: The string of the class of the activity that will be used as a lock/unlock screen.
	 * <p>The activity should return a resultCode of Activity.RESULT_OK to specify that
	 * an unlock completed successfully. Additionally, if the lockscreen returns an
	 * Activity.RESULT_CANCELLED, the current activity will be finished. All other
	 * result codes will result in lockscreen re-loading.</p>
	 */
	public static final String LOCKSCREEN_ACTIVITY_CLASS = "lockscreenActivityClass";
	
	/**
	 * Optional: A string array of classes paths that are not subject to the lockscreen.
	 * The LOCKSCREEN_ACTIVITY_CLASS is automatically added to this list.
	 */
	public static final String WHITELIST_ACTIVITY_CLASSES = "WhitelistActivityClasses";
	
	/**
	 * <p>Optional: A long that contains the amount of time (in milliseconds) before the app is locked.
	 * Set this if you want the app to remain unlocked when switching between other apps.
	 * After n milliseconds passes, the next time the app is accessed, it will be locked.</p>
	 * <p>Default is 60000 (1 minute). Set to -1 to disable.</p>
	 */
	public static final String INACTIVITY_TIMEOUT = "inactivityTimeout";
	
	/*
	 * Local constants.
	 */
	private static final String IS_LOCKED_PREF_KEY = "VGMod.AppLock.isLocked";
	private static final String IS_ENABLED_PREF_KEY = "VGMod.AppLock.isEnabled";
	private static final int LOCKSCREEN_ACTIVITY_REQUEST_CODE = 805843653;
	private static final boolean DEBUG = false;
	
	/*
	 * Module load arguments.
	 */
	private Class<? extends Activity> mLockScreenClass = null;
	private ArrayList<Class<? extends Activity>> mActivityWhiteList = new ArrayList<Class<? extends Activity>>();
	private long mInactivityTimeout = 0;
	private boolean mInactivityTimeoutEnabled = false;
	
	/*
	 * Local variables.
	 */
	private SharedPreferences mPrefs = null;
	private boolean mIsInAOnPause = false;
	private boolean mIsInAOnResume = false;
	private long mWentToBackgroundTime = 0;
	
	@Override
	public void load(Bundle args) {
		/*
		 * Get the lockscreen class.
		 */
		// get the class string and check if it is null.
		String lockScreenClassStr = args.getString(LOCKSCREEN_ACTIVITY_CLASS);
		if(lockScreenClassStr == null) {
			throw new IllegalArgumentException("LOCKSCREEN_ACTIVITY_CLASS needs to be set.");
		}
		
		// convert the class string to a class and check if that was successful.
		mLockScreenClass = getActivityClass(lockScreenClassStr);
		if(mLockScreenClass == null) {
			throw new IllegalArgumentException("LOCKSCREEN_ACTIVITY_CLASS needs extend Activity in some way and be visible. This module cannot load it.");
		}
		
		// we have a valid lockscreen class, add it to the whitelist.
		mActivityWhiteList.add(mLockScreenClass);
		
		/*
		 * Get the whitelist classes.
		 */
		// get the class strings and iterate through them.
		String[] whitelist = args.getStringArray(WHITELIST_ACTIVITY_CLASSES);
		if(whitelist != null) {
			for(String classPath: whitelist) {
				// if we could get a class from the provided string, add it to the whitelist.
				Class<? extends Activity> activityClass = getActivityClass(classPath);
				if(activityClass != null) {
					mActivityWhiteList.add(activityClass);
				}
			} // for
		} // if
		
		/*
		 * Get the inactivity timeout.
		 */
		mInactivityTimeout = args.getLong(INACTIVITY_TIMEOUT);
		if(mInactivityTimeout == 0) {
			mInactivityTimeout = 60000;
		}
		mInactivityTimeoutEnabled = mInactivityTimeout > 0;
	} // method
	
	@Override
	public void unLoad() {
		
	}

	public void onInvokeActivityOnCreate(Activity activity, Bundle savedInstanceState) {
		// set the prefs object.
		if(DEBUG)Log.v("TESTME", "onInvokeActivityOnCreate: "+activity.getClass().getSimpleName());
		mPrefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
	} // method

	public void onInvokeActivityOnActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		if(DEBUG)Log.v("TESTME", "onInvokeActivityOnActivityResult: "+activity.getClass().getSimpleName());
		
		// unlock successful, set unlocked.
		if(requestCode == LOCKSCREEN_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			setIsLocked(false);
			return;
 		}
		
		// the unlock screen was closed but this activity should be locked, treat that
		// as a close for this activity. This will start a cascade all the way
		// to the app exiting or until a persistent activity is found.
		if(isActivityShouldBeLocked(activity)) {
			activity.finish();
			return;
		}
	} // method
	
	public void onInvokeActivityOnResume(Activity activity) {
		if(DEBUG)Log.v("TESTME", "onInvokeActivityOnResume: "+activity.getClass().getSimpleName());
		
		mIsInAOnResume = true;
		
		// this activity is loading from background.
		boolean isLoadingFromBackground = this.mIsInAOnResume && !this.mIsInAOnPause;
		if(isLoadingFromBackground) {
			// if the app has been in the background for too long, lock the app.
			if(mInactivityTimeoutEnabled && getAppBackgroundDuration() > this.mInactivityTimeout) {
				this.setIsLocked(true);
			}
		}
		
		// if the activity should be locked, then start the lock screen.
		if(isActivityShouldBeLocked(activity)) {
			startLockActivity(activity);
		}
	}

	public void onInvokeActivityOnPause(Activity activity) {
		if(DEBUG)Log.v("TESTME", "onInvokeActivityOnPause: "+activity.getClass().getSimpleName());
		
		mIsInAOnPause = true;
	}
	
	public void onInvokeActivityOnStop(Activity activity) {
		if(DEBUG)Log.v("TESTME", "onInvokeActivityOnStop: "+activity.getClass().getSimpleName());
		
		boolean wentToBackground = this.mIsInAOnPause && !this.mIsInAOnResume;
		
		this.mIsInAOnPause = false;
		this.mIsInAOnResume = false;
		
		// App has gone into the background.
		if(wentToBackground) {
			// user user interaction as a means to determine if there has been activity.
			mWentToBackgroundTime = System.currentTimeMillis();
		}
	} // method
	
	/**
	 * Get an activity class from a string.
	 * @param classPath The class path of the activity.
	 * @return A Class object or null of something went wrong.
	 */
	@SuppressWarnings("unchecked")
	private static Class<? extends Activity> getActivityClass(String classPath) {
		try {
			// return the class.
			return (Class<? extends Activity>) Class.forName(classPath);
			
		// class doesn't exist.
		} catch (ClassNotFoundException e) {
			//ignore
			
		// other issues, most likely not an activity. (class cast exception)
		} catch (Exception e) {
			// ignore
		}
		
		// couldn't get the class.
		return null;
	} // method
	
	/**
	 * Checks if a class is in the white list.
	 * @param cls The class to search for.
	 * @return True is the lass in in the white list, false otherwise.
	 */
	private boolean isClassInWhiteList(Class<? extends Activity> cls) {
		return mActivityWhiteList.contains(cls);
	} // method
	
	/**
	 * Starts the lock activity.
	 * @param activity
	 */
	private void startLockActivity(Activity activity) {
		activity.startActivityForResult(
			new Intent(activity, mLockScreenClass), 
			LOCKSCREEN_ACTIVITY_REQUEST_CODE
		);
	} // method

	private long getAppBackgroundDuration() {
		return System.currentTimeMillis() - mWentToBackgroundTime;
	}

	private boolean isActivityShouldBeLocked(Activity activity) {
		// lock is not enabled, don't mess with the rest.
		if(!getIsEnabled()) {
			return false;
		}
		
		// if not locked, then let the activity load.
		if(!getIsLocked()) {
			return false;
		}
		
		// if activity class is on the white list, let activity load.
		if(isClassInWhiteList(activity.getClass())) {
			return false;
		}
		
		return true;
	}	
	
	/**
	 * Sets the status of the app as locked or not.
	 * @param l True if the app should be locked, false otherwise.
	 */
	public void setIsLocked(boolean l) {
		mPrefs.edit().putBoolean(IS_LOCKED_PREF_KEY, l).commit();
	}
	
	/**
	 * Checks whether or not the app is locked.
	 * @return True if the app is locked, false otherwise.
	 */
	public boolean getIsLocked() {
		return mPrefs.getBoolean(IS_LOCKED_PREF_KEY, false);
	}
	
	/**
	 * Set if the lock mechanisims are enabled.
	 * @param e True to disable app locking, false otherwise.
	 */
	public void setIsEnabled(boolean e) {
		mPrefs.edit().putBoolean(IS_ENABLED_PREF_KEY, e).commit();
	}
	
	/**
	 * Check if app lock is enabled.
	 * @return True if app lock is enabled, false otherwise.
	 */
	public boolean getIsEnabled() {
		return mPrefs.getBoolean(IS_ENABLED_PREF_KEY, true);
	}

} // class