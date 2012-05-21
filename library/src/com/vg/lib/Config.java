package com.vg.lib;

import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

public class Config {
	
	public static class Package {
		private static Bundle packageMetaData = null;
		private static final HashMap<String,Boolean> booleanCache = new HashMap<String,Boolean>();
		private static final HashMap<String,String> stringCache = new HashMap<String,String>();
		private static final HashMap<String,Integer> integerCache = new HashMap<String,Integer>();
		
		public static int getIntegerMeta(Context c, String name) {
			Integer ret = integerCache.get(name);
			
			if(ret == null) {
				Bundle data = getPackageMetaData(c);
				if(data == null) {
					ret = -1;
				} else {
					ret = data.getInt(name, -1);
				}
			}
			
			return ret;
		}
		
		public static boolean getBooleanMeta(Context c, String name) {
			Boolean ret = booleanCache.get(name);
			
			if(ret == null) {
				Bundle data = getPackageMetaData(c);
				if(data == null) {
					ret = false;
				} else {
					ret = data.getBoolean(name, true);
				}
			}
			
			return ret;
		}
		
		public static String getStringMeta(Context c, String name) {
			String ret = stringCache.get(name);
			
			if(name == null) {
				Bundle data = getPackageMetaData(c);
				if(data == null) {
					ret = null;
				} else {
					ret = data.getString(name);
				}
			}
			
			return ret;
		}
		
		private static Bundle getPackageMetaData(Context c) {
			ApplicationInfo pii;
			if(packageMetaData == null) {
				try {
					pii = c.getPackageManager().getApplicationInfo(c.getPackageName(), PackageManager.GET_META_DATA);
					packageMetaData = pii.metaData;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
			}
			return packageMetaData;
		}
	}
}
