package com.vg.lib.modules.googleanalytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.vg.lib.module.Module;
import com.vg.lib.module.BaseModuleImpl;
import com.vg.lib.module.ModuleRuntimeException;

/**
 * Provides automatic Google Analytics support to your activity.
 * Just load the module from your activity's onLoad method and
 * this module will send page events to google analytics with the
 * classname of the current activity.
 * Note: google analytics does require a few permissions, consult
 * the docs.
 * @author vangorra
 *
 */
public class GoogleAnalyticsModule extends BaseModuleImpl {
	/**
	 * Argument specifying the google analytics tracker id (key) to use.
	 * This is required.
	 */
	public static final String TRACKER_ID = "tracker_id";
	
	/**
	 * Argument specifying how often to bulk send analytics data in seconds.
	 * Default: 20
	 * Optional.
	 */
	public static final String TACKER_DISPATCH_INTERVAL = "dispatchInterval";
	
	private int dispatchInterval = 20;
	private String trackerId = null;
	private GoogleAnalyticsTracker tracker;

	/**
	 * Returns the analytics tracker currently in use. This is useful
	 * if you want to send some custom events and such.
	 * @return Google Analytics tracker.
	 */
	public GoogleAnalyticsTracker getTracker() {
		return this.tracker;
	}
	
	@Override
	public void load(Context context, Bundle args) {
		/**
		 * Configure the tracker id.
		 */
		// get the tracker id from the arugments.
		trackerId = args.getString(TRACKER_ID);
		
		// if the tracker id was not set.
		if(trackerId == null) {
			// throw exception.
			throw new ModuleRuntimeException("TRACKER_ID needs to be set when loading this module.");
		}
		
		/**
		 * Configure the dispatch interval.
		 */
		// get the dispatch interval from the arguments.
		int dI = args.getInt(TACKER_DISPATCH_INTERVAL);
		
		// if the interval is valid.
		if(dI > 0) {
			// set it.
			this.dispatchInterval = dI;
		}
	} // method

	@Override
	public void unLoad() {
		// nothing to do here.
	}

	@Override
	public void onActivityCreate(Activity activity, Bundle savedInstanceState) {
		super.onActivityCreate(activity, savedInstanceState);
		
		// initialize a new tracker.
		this.tracker = GoogleAnalyticsTracker.getInstance();
		
		// configure the tracker by starting a new session.
		this.tracker.startNewSession(this.trackerId, this.dispatchInterval, activity);
		
		// track the pageview with the activity's class name.
		this.tracker.trackPageView(activity.getLocalClassName());
		
		// send the event to google.
		this.tracker.dispatch();		
	} //method

	@Override
	public void onActivityDestroy(Activity activity) {
		super.onActivityDestroy(activity);
		
		// if the tracker is set.
		if(this.tracker != null) {
			// tell it the session has stopped.
			this.tracker.stopSession();
		}
	} //method
} //class
