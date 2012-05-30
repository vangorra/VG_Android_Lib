package com.vg.lib.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * Provides utilities for working with Intent objects.
 * @author vangorra
 *
 */
public final class IntentUtils {
	/**
	 * Query for all apps that will handle the given intent.
	 * @param context The activity context.
	 * @param i The intent in question.
	 * @return A list of ResolveInfo representing apps that support the intent.
	 */
	public static List<ResolveInfo> queryAppsSupportingIntent(Context context, Intent intent) {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(intent == null) {
			throw new IllegalArgumentException("intent cannot be null");
		}
		
		// get the package manager.
		PackageManager manager = context.getPackageManager();
		
		// query and return the results.
		return manager.queryIntentActivities(intent, 0);
	} // method
} // class
