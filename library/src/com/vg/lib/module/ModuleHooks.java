package com.vg.lib.module;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Provides all the hooks a module can currently listen for.
 * @author vangorra
 *
 */
public interface ModuleHooks {
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
	
	/**
	 * Called when an activity's onActivityResult method is called.
	 * @param activity the activity
	 * @param requestCode The integer request code originally supplied to startActivityForResult(), allowing you to identify who this result came from.
	 * @param resultCode The integer result code returned by the child activity through its setResult()
	 * @param data An Intent, which can return result data to the caller (various data can be attached to Intent "extras").
	 */
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);
	
	/**
	 * Called whenever a key, touch, or trackball event is dispatched to the activity. Implement this method if you wish to know that the user has interacted with the device in some way while your activity is running. This callback and onUserLeaveHint() are intended to help activities manage status bar notifications intelligently; specifically, for helping activities determine the proper time to cancel a notfication.
	 * All calls to your activity's onUserLeaveHint() callback will be accompanied by calls to onUserInteraction(). This ensures that your activity will be told of relevant user activity such as pulling down the notification pane and touching an item there.
	 * Note that this callback will be invoked for the touch down action that begins a touch gesture, but may not be invoked for the touch-moved and touch-up actions that follow.
	 * @param activity the activity that called the method.
	 */
	public void onActivityUserInteraction(Activity activity);
}
