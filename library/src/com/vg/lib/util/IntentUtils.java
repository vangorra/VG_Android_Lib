package com.vg.lib.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

public class IntentUtils {
	public static List<ResolveInfo> queryAppsSupportingUri(Context context, Uri uri) {
		Intent tmpIntent = new Intent(Intent.ACTION_VIEW, uri);
		PackageManager manager = context.getPackageManager();
		return manager.queryIntentActivities(tmpIntent, 0);
	}
	
	private static boolean isMarketIntentSet = false;
	private static Intent marketIntent = null;
	public static Intent getMarketIntent(Context context) {
		if(isMarketIntentSet) {
			return marketIntent;
		}
		
		String packageName = context.getApplicationContext().getPackageName();
		Uri androidMarket = Uri.parse("market://details?id=" + packageName);
		Uri amazonAppStore = Uri.parse("http://www.amazon.com/gp/mas/dl/android?"+ packageName);
		Intent intent = null;
		
		if(queryAppsSupportingUri(context, androidMarket).size() > 0) {
			intent = new Intent(Intent.ACTION_VIEW, androidMarket);
			
		} else if(queryAppsSupportingUri(context, amazonAppStore).size() > 0) {
			intent = new Intent(Intent.ACTION_VIEW, amazonAppStore);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		
		isMarketIntentSet = true;
		marketIntent = intent;
		return marketIntent;
	}
}
