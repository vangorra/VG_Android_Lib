package com.vg.lib.modules.applock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.vg.lib.module.ModuleImpl;

/**
 * Facilitates locking and unlocking of an app based on inactivity or loss of focus.
 * This module can be configured with lockscreen activity to use for user presentation.
 * 
 * Example:
 * <pre>
 * public void onModuleLoad() {
 *   Bundle args = new Bundle();
 *   args.putString(
 *     AppLock.LOCKSCREEN_ACTIVITY_CLASS,
 *     com.yourcompany.activity.MyLockScreen.class.getName()
 *   ); 
 *   
 *   ModuleManager.getInstance(this).load(
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
public class AppLock extends ModuleImpl {
	/**
	 * The string of the class of the activity that will be used as a lock/unlock screen.
	 * <p>The activity should return a resultCode of Activity.RESULT_OK to specify that
	 * an unlock completed successfully. Additionally, if the lockscreen returns an
	 * Activity.RESULT_CANCELLED, the current activity will be finished. All other
	 * result codes will result in lockscreen re-loading.</p>
	 */
	public static final String LOCKSCREEN_ACTIVITY_CLASS = "lockscreenActivityClass";
	
	/**
	 * A string array of classes paths that are not subject to the lockscreen.
	 * The LOCKSCREEN_ACTIVITY_CLASS is automatically added to this list.
	 */
	public static final String WHITELIST_ACTIVITY_CLASSES = "WhitelistActivityClasses";
	
	/**
	 * A long that contains the amount of time (in milliseconds) before the app is locked.
	 * Set this if you want the app to remain unlocked when switching between other apps.
	 * After n milliseconds passes, the next time the app is accessed, it will be locked.
	 */
	public static final String INACTIVITY_TIMEOUT = "inactivityTimeout";
	
	/*
	 * Local constants.
	 */
	private static final String IS_LOCKED_PREF_KEY = "VGMod.AppLock.isLocked";
	private static final String IS_ENABLED_PREF_KEY = "VGMod.AppLock.isEnabled";
	private static final String TAG = AppLock.class.getName();
	private static final int LOCKSCREEN_ACTIVITY_REQUEST_CODE = 805843;
	
	/*
	 * Module load arguments.
	 */
	private Class<? extends Activity> lockScreenClass = null;
	private ArrayList<Class<? extends Activity>> activityWhiteList = new ArrayList<Class<? extends Activity>>();
	private long inactivityTimeout = 0;
	private boolean inactivityTimeoutEnabled = false;
	
	/*
	 * Local variables.
	 */
	private SharedPreferences prefs = null;
	private long lastActivity = 0;
	
	@Override
	public void load(Context context, Bundle args) {
		/*
		 * Get the lockscreen class.
		 */
		// get the class string and check if it is null.
		String lockScreenClassStr = args.getString(LOCKSCREEN_ACTIVITY_CLASS);
		if(lockScreenClassStr == null) {
			throw new IllegalArgumentException("LOCKSCREEN_ACTIVITY_CLASS needs to be set.");
		}
		
		// convert the class string to a class and check if that was successful.
		lockScreenClass = getActivityClass(lockScreenClassStr);
		if(lockScreenClass == null) {
			throw new IllegalArgumentException("LOCKSCREEN_ACTIVITY_CLASS needs extend Activity in some way and be visible. This module cannot load it.");
		}
		
		// we have a valid lockscreen class, add it to the whitelist.
		activityWhiteList.add(lockScreenClass);
		
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
					activityWhiteList.add(activityClass);
				}
			} // for
		} // if
		
		/*
		 * Get the inactivity timeout.
		 */
		inactivityTimeout = args.getLong(INACTIVITY_TIMEOUT);
		inactivityTimeoutEnabled = inactivityTimeout > 0;
	} // method
	
	@Override
	public void unLoad() {
		
	}

	/**
	 * Sets the status of the app as locked or not.
	 * @param l True if the app should be locked, false otherwise.
	 */
	public void setIsLocked(boolean l) {
		prefs.edit().putBoolean(IS_LOCKED_PREF_KEY, l).commit();
	}
	
	/**
	 * Checks whether or not the app is locked.
	 * @return True if the app is locked, false otherwise.
	 */
	public boolean getIsLocked() {
		return prefs.getBoolean(IS_LOCKED_PREF_KEY, false);
	}
	
	/**
	 * Set if the lock mechanisims are enabled.
	 * @param e True to disable app locking, false otherwise.
	 */
	public void setIsEnabled(boolean e) {
		prefs.edit().putBoolean(IS_ENABLED_PREF_KEY, e).commit();
	}
	
	/**
	 * Check if app lock is enabled.
	 * @return True if app lock is enabled, false otherwise.
	 */
	public boolean getIsEnabled() {
		return prefs.getBoolean(IS_ENABLED_PREF_KEY, true);
	}
	
	/**
	 * Get an activity class from a string.
	 * @param classPath The class path of the activity.
	 * @return A Class object or null of something went wrong.
	 */
	private static Class<? extends Activity> getActivityClass(String classPath) {
		try {
			// return the class.
			return (Class<? extends Activity>) Class.forName(classPath);
			
		// class doesn't exist.
		} catch (ClassNotFoundException e) {
			Log.e(TAG, "Class path: "+ classPath +" could not be found.");
			
		// other issues, most likely not an activity. (class cast exception)
		} catch (Exception e) {
			Log.e(TAG, "Class path: "+ classPath +" doesn't extend Activity in some way.");
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
		return activityWhiteList.contains(cls);
	} // method
	
	/**
	 * Starts the lock activity.
	 * @param activity
	 */
	private void startLockActivity(Activity activity) {
		activity.startActivityForResult(
			new Intent(activity, lockScreenClass), 
			LOCKSCREEN_ACTIVITY_REQUEST_CODE
		);
	} // method
	
	/**
	 * Get how long the app has been inactive.
	 * @return The length of time (milliseconds) the app has been inactive.
	 */
	private long getInactivtyDuration() {
		return Calendar.getInstance().getTimeInMillis() - lastActivity;
	}
	
	// pulled from http://stackoverflow.com/questions/4414171/how-to-detect-when-an-android-app-goes-to-the-background-and-come-back-to-the-fo
	/**
	 * Checks if the app is no longer in front.
	 * @param context
	 * @return
	 */
	private boolean isApplicationBroughtToBackground(Context context) {
	    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	    if (!tasks.isEmpty()) {
	        ComponentName topActivity = tasks.get(0).topActivity;
	        if (!topActivity.getPackageName().equals(context.getPackageName())) {
	            return true;
	        }
	    }

	    return false;
	} // method
	
	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		super.onActivityCreate(activity, savedInstanceState);
		
		// set the prefs object.
		prefs = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
		
		// lock is not enabled, don't mess with the rest.
		if(!getIsEnabled()) {
			return;
		}
		
		// if not locked, then let the activity load.
		if(!getIsLocked()) {
			return;
		}
		
		// if activity class is on the white list, let activity load.
		if(isClassInWhiteList(activity.getClass())) {
			return;
		}
		
		// start the lock activity.
		startLockActivity(activity);
	} // method
	
	@Override
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
		super.onActivityResult(activity, requestCode, resultCode, data);
		
		// lock is not enabled, don't mess with the rest.
		if(!getIsEnabled()) {
			return;
		}
		
		// if the request code is not what we sent, then we know this is not from the lock activity.
		if(requestCode != LOCKSCREEN_ACTIVITY_REQUEST_CODE) {
			return;
		}
		
		// successfully unlocked
		if(resultCode == Activity.RESULT_OK) {
			setIsLocked(false);
			
		// unlock cancelled, finish this activity.
		} else if(resultCode == Activity.RESULT_CANCELED) {
			activity.finish();
			
		// unlock not successful. start the lock activity again.
		} else {
			startLockActivity(activity);
		}
	} // method

	@Override
	public void onActivityUserInteraction(Activity activity) {
		super.onActivityUserInteraction(activity);
		
		// user user interaction as a means to determine if there has been activity.
		lastActivity = Calendar.getInstance().getTimeInMillis();
	} // method

	@Override
	public void onActivityPause(Activity activity) {
		super.onActivityPause(activity);
		
		// App has gone into the background.
		if(isApplicationBroughtToBackground(activity)) {
			// if inactivity timeout is enabled.
			if(inactivityTimeoutEnabled) {
				// if the app has been inactive for inactivityTimeout milliseconds, lock the app.
				if(getInactivtyDuration() > inactivityTimeout) {
					setIsLocked(true);
				}
				
			// just lock the app.
			} else {
				setIsLocked(true);
			}
		} // if
	} // method
} // class
