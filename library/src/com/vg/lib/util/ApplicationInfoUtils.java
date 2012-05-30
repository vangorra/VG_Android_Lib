package com.vg.lib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

/**
 * Provides high level objects for working with application data.
 * @author vangorra
 *
 */
public final class ApplicationInfoUtils {
	/**
	 * Provides access to the meta-data tag values written within the application block of the manifest file.
	 * @author vangorra
	 *
	 */
	public static final class MetaData {
		
		private static Bundle packageMetaData = null;
		
		/**
		 * Get an integer meta-data value from the application's manifest file.
		 * @param c The context of the activity.
		 * @param name The name of the meta-data element.
		 * @param defaultValue The value to return if the meta-data element is not set.
		 * @return The value of the meta-data element or the provided default value if no value could be found or type mapping could not succeed.
		 */
		public static int getIntegerMeta(Context c, String name, int defaultValue) {
			// check for null
			if(c == null) {
				throw new IllegalArgumentException("context cannot be null.");
			}
			
			//check for null
			if(name == null) {
				throw new IllegalArgumentException("name cannot be null.");
			}
			
			// get the meta-data bundle.
			Bundle data = getPackageMetaData(c);
			
			// return the default value if the meta-data bundle is not set.
			if(data == null) {
				return defaultValue;
			}
		
			// get the object from the bundle.
			Object ret = data.get(name);
			
			// value is not set, return the default.
			if(ret == null) {
				return defaultValue;
			}
			
			// cast to an int.
			try {
				return (Integer)ret;
				
			// failed to cast, this type cannot be converted, return default.
			} catch (ClassCastException cce) {
				return defaultValue;
			}
		} // method
		
		/**
		 * Get an integer meta-data value from the application's manifest file.
		 * @param c The context of the activity.
		 * @param name The name of the meta-data element.
		 * @param defaultValue The value to return if the meta-data element is not set.
		 * @return The value of the meta-data element or the provided default value if no value could be found or type mapping could not succeed.
		 */
		public static float getFloatMeta(Context c, String name, float defaultValue) {
			// check for null
			if(c == null) {
				throw new IllegalArgumentException("context cannot be null.");
			}
			
			//check for null
			if(name == null) {
				throw new IllegalArgumentException("name cannot be null.");
			}
			
			// get the meta-data bundle.
			Bundle data = getPackageMetaData(c);
			
			// return the default value if the meta-data bundle is not set.
			if(data == null) {
				return defaultValue;
			}
		
			// get the object from the bundle.
			Object ret = data.get(name);
			
			// value is not set, return the default.
			if(ret == null) {
				return defaultValue;
			}
			
			// cast to an int.
			try {
				return (Float)ret;
				
			// failed to cast, this type cannot be converted, return default.
			} catch (ClassCastException cce) {
				return defaultValue;
			}
		} // method
		
		/**
		 * Get a boolean meta-data value from the application's manifest file.
		 * @param c The context of the activity.
		 * @param name The name of the meta-data element.
		 * @param defaultValue The value to return if the meta-data element is not set.
		 * @return The value of the meta-data element or the provided default value if no value could be found or type mapping could not succeed.
		 */
		public static boolean getBooleanMeta(Context c, String name, boolean defaultValue) {
			// check for null
			if(c == null) {
				throw new IllegalArgumentException("context cannot be null.");
			}
			
			//check for null
			if(name == null) {
				throw new IllegalArgumentException("name cannot be null.");
			}
			
			// get the meta-data bundle.
			Bundle data = getPackageMetaData(c);
			
			// return the default value if the meta-data bundle is not set.
			if(data == null) {
				return defaultValue;
			}
		
			// get the object from the bundle.
			Object ret = data.get(name);
			
			// value is not set, return the default.
			if(ret == null) {
				return defaultValue;
			}
			
			// cast to a boolean.
			try {
				return (Boolean)ret;
				
			// failed to cast, this type cannot be converted, return default.
			} catch (ClassCastException cce) {
				return defaultValue;
			}
		} // method
		
		/**
		 * Get a string meta-data value from the application's manifest file.
		 * @param c The context of the activity.
		 * @param name The name of the meta-data element.
		 * @param defaultValue The value to return if the meta-data element is not set.
		 * @return The value of the meta-data element or the provided default value if no value could be found or type mapping could not succeed.
		 */
		public static String getStringMeta(Context c, String name, String defaultValue) {
			// check for null
			if(c == null) {
				throw new IllegalArgumentException("context cannot be null.");
			}
			
			//check for null
			if(name == null) {
				throw new IllegalArgumentException("name cannot be null.");
			}
			
			// get the meta-data bundle.
			Bundle data = getPackageMetaData(c);
			
			// return the default value if the meta-data bundle is not set.
			if(data == null) {
				return defaultValue;
			}
		
			// get the object from the bundle.
			Object ret = data.get(name);
			
			// value is not set, return the default.
			if(ret == null) {
				return defaultValue;
			}
			
			// cast to a String.
			try {
				return (String)ret;
				
			// failed to cast, this type cannot be converted, return default.
			} catch (ClassCastException cce) {
				return defaultValue;
			}
		} // method
		
		/**
		 * Gets the package meta data for a given context.
		 * @param c The context of the activity.
		 * @return  A bundle containing the meta-data.
		 */
		public static Bundle getPackageMetaData(Context c) {
			// check for null
			if(c == null) {
				throw new IllegalArgumentException("context cannot be null.");
			}
			
			// get the application info object.
			ApplicationInfo pii = getApplicationInfo(c);
			
			// if nothing was returned, then return null.
			if(pii == null) {
				return null;
			}
			
			// load meta data from cache.
			if(packageMetaData != null) {
				return packageMetaData;
			}
			
			// set cache set return meta data bundle.
			packageMetaData = pii.metaData;
			return packageMetaData;
		} // method
		
		/**
		 * Return the application information about the current context.
		 * @param c The context of the current activity.
		 * @return The application info for the currently running app.
		 */
		private static ApplicationInfo getApplicationInfo(Context c) {
			// check for null
			if(c == null) {
				throw new IllegalArgumentException("context cannot be null.");
			}
			
			// get the application information from the current context.
			try {
				return c.getPackageManager().getApplicationInfo(
						c.getPackageName(), 
						PackageManager.GET_META_DATA
				);
				
			} catch (NameNotFoundException e) {
				return null;
			}
		} // method

	} // class
} // class
