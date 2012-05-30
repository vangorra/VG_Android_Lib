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
	public static List<ResolveInfo> queryAppsSupportingIntent(Context context, Intent i) {
		// get the package manager.
		PackageManager manager = context.getPackageManager();
		
		// query and return the results.
		return manager.queryIntentActivities(i, 0);
	} // method
} // class
