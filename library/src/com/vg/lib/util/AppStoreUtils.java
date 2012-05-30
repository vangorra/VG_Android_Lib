package com.vg.lib.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

/**
 * Provides a set of methods for checking and launching app stores.
 * @author vangorra
 *
 */
public final class AppStoreUtils {
	/**
	 * The available app stores.
	 * @author vangorra
	 *
	 */
	public static enum Store {
		GOOGLE,
		AMAZON,
	};

	/**
	 * Mapping of the supported stores.
	 */
	public static final Store[] supportedStores = new Store[]{
		Store.GOOGLE,
		Store.AMAZON,
	};
	
	/**
	 * Mapping of the supported stores and their base uris.
	 */
	private static final String[] storeBaseUris = new String[] {
		"market://",
		"http://www.amazon.com/gp/mas/dl/android",
	};
	
	/**
	 * Return an array of available stores.
	 * @param context The context to use.
	 * @return An array of supported stores. If no stores were found, then an empty array will be returned.
	 */
	public static Store[] getAvailableStores(Context context) {
		ArrayList<Store> stores = new ArrayList<Store>();
		
		// iterate through each store.
		for(Store s: supportedStores) {
			// if it is available, then add to the list.
			if(isStoreAvailable(context, s)) {
				stores.add(s);
			}
		}
		
		// return the available stores as an array.
		return stores.toArray(new Store[stores.size()]);
	}
	
	/**
	 * Checks if an specific app store is available on this device.
	 * @param context The activity context.
	 * @param store The store to check for availability.
	 * @return True if the app store is available, false otherwise.
	 */
	public static boolean isStoreAvailable(Context context, Store store) {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(store == null) {
			throw new IllegalArgumentException("store cannot be null");
		}
		
		// get the store uri, return if store is not supported.
		String storeUri = getStoreBaseUriString(store);
		if(storeUri == null) {
			return false;
		}
		
		// return if the store is available.
		return _isAppStoreAvailable(context, store);
	} // method.
	
	/**
	 * Get the search Uri for a specific app store.
	 * @param store The app store
	 * @param query The search query.
	 * @return The constructed search uri for the specified app store.
	 */
	public static Uri getSearchUri(Store store, String query) {
		// check for null
		if(store == null) {
			throw new IllegalArgumentException("store cannot be null");
		}
		
		// check for null
		if(query == null) {
			throw new IllegalArgumentException("query cannot be query");
		}
		
		return _getSearchUri(store, query);
	} // method
	
	/**
	 * Get an app store intent.
	 * @param store The app store.
	 * @param query The search query
	 * @return The intent for the provided store.
	 */
	public static Intent getSearchIntent(Store store, String query) {
		// check for null
		if(store == null) {
			throw new IllegalArgumentException("store cannot be null");
		}
		
		// check for null
		if(query == null) {
			throw new IllegalArgumentException("query cannot be query");
		}
		
		// init the intent.
		Intent intent = new Intent(Intent.ACTION_VIEW, getSearchUri(store, query));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	} // method
	
	/**
	 * Get the search Uri for a specific app store.
	 * @param store The app store
	 * @param query The search query.
	 * @return The constructed search uri for the specified app store.
	 */
	public static Uri getDetailsUri(Store store, String packageName) {
		// check for null
		if(store == null) {
			throw new IllegalArgumentException("store cannot be null");
		}
		
		// check for null
		if(packageName == null) {
			throw new IllegalArgumentException("packageName cannot be query");
		}
		
		return _getDetailsUri(store, packageName);
	} // method
	
	/**
	 * Get an app store intent.
	 * @param store The app store.
	 * @param packageName The package name of the app in question.
	 * @return The intent for the provided store.
	 */
	public static Intent getDetailsIntent(Store store, String packageName) {
		// check for null
		if(store == null) {
			throw new IllegalArgumentException("store cannot be null");
		}
		
		// check for null
		if(packageName == null) {
			throw new IllegalArgumentException("packageName cannot be query");
		}
		
		// init the intent.
		Intent intent = new Intent(Intent.ACTION_VIEW, getDetailsUri(store, packageName));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return intent;
	} // method
	
	/**
	 * Get the base string uri for a given store.
	 * @param store
	 * @return
	 */
	private static String getStoreBaseUriString(Store store) {
		// iterate through all the supported stores.
		for(int i = 0; i < supportedStores.length; ++i) {
			// if we are looking at the store, return the same element in the base uris array.
			if(supportedStores[i] == store) {
				return storeBaseUris[i];
			}
		}
		return null;
	} // method
	

	/*
	 * 
	 * Methods which provide store specific conditions. 
	 * 
	 */
	
	/**
	 * Provides conditional wrappers for a store.
	 * @param context
	 * @param store
	 * @return
	 */
	private static boolean _isAppStoreAvailable(Context context, Store store) {
		// init
		Intent appIntent = null;
		PackageManager manager = context.getPackageManager();
		
		// if the store is amazon, check for the actual app.
		if(store == Store.AMAZON) {
			appIntent = manager.getLaunchIntentForPackage("com.amazon.mShop.android");

		// otherwise, build a search string.
		} else {
			appIntent = new Intent(Intent.ACTION_VIEW, getSearchUri(store, "test"));
		}
		
		// set the intent to open as its own task.
		appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
		// query for applications that support the intent.
		List<ResolveInfo> intentActivities = IntentUtils.queryAppsSupportingIntent(context, appIntent);
		
		// if nothing was found, then return false.
		if(intentActivities == null || intentActivities.size() <= 0) {
			return false;
		}
		
		// yep, this store is available.
		return true;
	} // method
	
	/**
	 * Provides conditional wrappers for a store.
	 * @param store
	 * @param query
	 * @return
	 */
	private static Uri _getSearchUri(Store store, String query) {
		// get the store uri, return if store is not supported.
		String storeUri = getStoreBaseUriString(store);
		if(storeUri == null) {
			return null;
		}
		
		// handle google search uri
		if(store == Store.GOOGLE) {
			return Uri.parse(storeUri + "search?q="+ query);
			
		// handle amazon search uri
		} else if(store == Store.AMAZON) {
			return Uri.parse(storeUri + "?s="+ query);
		}
		
		return null;
	} // method
	
	/**
	 * Provides conditional wrappers for a store.
	 * @param store
	 * @param packageName
	 * @return
	 */
	private static Uri _getDetailsUri(Store store, String packageName) {
		// get the store uri, return if store is not supported.
		String storeUri = getStoreBaseUriString(store);
		if(storeUri == null) {
			return null;
		}
		
		// handle google details uri.
		if(store == Store.GOOGLE) {
			return Uri.parse(storeUri + "details?id="+ packageName);
			
		// handle amazon details uri.
		} else if(store == Store.AMAZON) {
			return Uri.parse(storeUri + "?p="+ packageName);
		}
		
		return null;
	} // method
} // class
