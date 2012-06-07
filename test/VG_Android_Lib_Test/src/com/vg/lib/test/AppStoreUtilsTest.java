package com.vg.lib.test;

import com.vg.lib.util.AppStoreUtils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.test.AndroidTestCase;

public class AppStoreUtilsTest extends AndroidTestCase {
	public void testGetAvailableStores() {
		try {
			AppStoreUtils.getAvailableStores(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		AppStoreUtils.Store[] availableStores = AppStoreUtils.getAvailableStores(getContext());
		
		assertNotNull(availableStores);
		
		// this is on the emulator. There should be no available stores.
		if(Build.PRODUCT.equals("google_sdk") || Build.PRODUCT.equals("sdk")) {
			assertEquals(availableStores.length, 0);
			
		// this on a device. There should be at least 1 available store.
		} else {
			assertTrue(availableStores.length > 0);
		}
	}
	
	public void testIsStoreAvailable() {
		
	}
	
	public void testGetSearchUri() {
		try {
			AppStoreUtils.getSearchUri(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			AppStoreUtils.getSearchUri(AppStoreUtils.Store.GOOGLE, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		Uri uri;
		
		uri = AppStoreUtils.getSearchUri(AppStoreUtils.Store.GOOGLE, "test");
		assertNotNull(uri);
		assertTrue(uri.toString().equals("market://search?q=test"));
		
		uri = AppStoreUtils.getSearchUri(AppStoreUtils.Store.AMAZON, "test");
		assertNotNull(uri);
		assertTrue(uri.toString().equals("http://www.amazon.com/gp/mas/dl/android?s=test"));
	}
	
	public void testGetSearchIntent() {
		try {
			AppStoreUtils.getSearchIntent(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			AppStoreUtils.getSearchIntent(AppStoreUtils.Store.GOOGLE, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		Intent intent;
		
		intent = AppStoreUtils.getSearchIntent(AppStoreUtils.Store.GOOGLE, "com.test.me");
		assertNotNull(intent);
		assertTrue(intent.getAction().equals(Intent.ACTION_VIEW));
		
		intent = AppStoreUtils.getSearchIntent(AppStoreUtils.Store.AMAZON, "com.test.me");
		assertNotNull(intent);
		assertTrue(intent.getAction().equals(Intent.ACTION_VIEW));
	}
	
	public void testGetDetailsUri() {
		try {
			AppStoreUtils.getDetailsUri(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			AppStoreUtils.getDetailsUri(AppStoreUtils.Store.GOOGLE, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		Uri uri;
		
		uri = AppStoreUtils.getDetailsUri(AppStoreUtils.Store.GOOGLE, "com.test.me");
		assertNotNull(uri);
		assertTrue(uri.toString().equals("market://details?id=com.test.me"));
		
		uri = AppStoreUtils.getDetailsUri(AppStoreUtils.Store.AMAZON, "com.test.me");
		assertNotNull(uri);
		assertTrue(uri.toString().equals("http://www.amazon.com/gp/mas/dl/android?p=com.test.me"));
	}
	
	public void testGetDetailsIntent() {
		try {
			AppStoreUtils.getDetailsIntent(null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		try {
			AppStoreUtils.getDetailsIntent(AppStoreUtils.Store.GOOGLE, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		Intent intent;
		
		intent = AppStoreUtils.getDetailsIntent(AppStoreUtils.Store.GOOGLE, "com.test.me");
		assertNotNull(intent);
		assertTrue(intent.getAction().equals(Intent.ACTION_VIEW));
		
		intent = AppStoreUtils.getDetailsIntent(AppStoreUtils.Store.AMAZON, "com.test.me");
		assertNotNull(intent);
		assertTrue(intent.getAction().equals(Intent.ACTION_VIEW));
	}
}
