package com.vg.lib.module;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Interface describing the possible methods available for a module.
 * @author vangorra
 *
 */
public interface Module {
	/**
	 * Called when a module instance is being loaded.
	 * This is specific to the module system.
	 * @param context the current activity context.
	 * @param args The arguments to send to the module.
	 */
	public void load(Context context, Bundle args);
	
	/**
	 * Called when the module is being unloaded.
	 * This is specific to the module system.
	 */
	public void unLoad();
	
	/**
	 * Called when an activity's onCreate method is called.
	 * @param activity The activity
	 * @param savedInstanceState The argument received by the activity's onCreate method.
	 */
	public void onActivityCreate(Activity activity, Bundle savedInstanceState);
	
	/**
	 * Called when an activity's onPostCreate method is called.
	 * @param activity the activity
	 * @param savedInstanceState The argument received by the activity's onPostCreate method.
	 */
	public void onActivityPostCreate(Activity activity, Bundle savedInstanceState);
	
	/**
	 * Called when an activity's onSaveInstanceState method is called.
	 * @param activity the activity
	 * @param outState The argument received by the activity's onSaveInstaceState method.
	 */
	public void onActivitySaveInstanceState(Activity activity, Bundle outState);
	
	/**
	 * Called when an activity's onActivityPause method is called.
	 * @param activity the activity
	 */
	public void onActivityPause(Activity activity);
	
	/**
	 * Called when an activity's onActivityRestart method is called.
	 * @param activity the activity
	 */
	public void onActivityRestart(Activity activity);
	
	/**
	 * Called when an activity's onActivityDestroy method is called.
	 * @param activity the activity
	 */
	public void onActivityDestroy(Activity activity);
	
	/**
	 * Called when an activity's onActivityResume method is called.
	 * @param activity the activity
	 */
	public void onActivityResume(Activity activity);
	
	/**
	 * Called when an activity's onActivityStart method is called.
	 * @param activity the activity
	 */
	public void onActivityStart(Activity activity);
	
	/**
	 * Called when an activity's onActivityStop method is called.
	 * @param activity the activity
	 */
	public void onActivityStop(Activity activity);
} //interface
