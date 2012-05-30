package com.vg.lib.test;

import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.test.AndroidTestCase;

import com.vg.lib.graphics.BitmapFactory;

public class BitmapFactoryTest extends AndroidTestCase {
	public void testDecodeURL() {
		/*
		// test null input.
		try {
			BitmapFactory.decodeURL(null);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException iae) {
			assertTrue(true);
		}
		
		// test bad url
		try {
			assertNull(
				"This should have returned null",
				BitmapFactory.decodeURL(new URL("http://www.sdlkfselkvsljsdljewfsnstlktlldncnc.com/blahstuff.png"))
			);
		} catch (MalformedURLException me) {
			assertTrue(me.getMessage(), false);
		}
		
		// test getting a url.
		try {
			Bitmap bitmap = BitmapFactory.decodeURL(new URL("http://www.google.com/images/google_favicon_128.png"));
			assertNotNull(
					"Should have recieved a bitmap", 
					bitmap
			);
		} catch (MalformedURLException me) {
			assertTrue(me.getMessage(), false);
		}
		
		// test null input with valid url.
		try {
			BitmapFactory.decodeURL(new URL("http://www.google.com/images/google_favicon_128.png"), null);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException me) {
			assertTrue(true);
		} catch (MalformedURLException e) {
			assertTrue(false);
		}
		*/
	} // method
	
	public void testDecodeAsset() {
		// test null input.
		try {
			BitmapFactory.decodeAsset(null, null);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException iae) {
			assertTrue(true);
		}
		
		// test null input.
		try {
			BitmapFactory.decodeAsset(this.getContext(), null);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException iae) {
			assertTrue(true);
		}
		
		// test decode of empty asset.
		assertNull(
			"Bitmap should not have decoded decode.", 
			BitmapFactory.decodeAsset(
					this.getContext(), 
					""
			)
		);
		
		// test decode of asset.
		assertNotNull(
			"Bitmap did not decode.", 
			BitmapFactory.decodeAsset(
					this.getContext(), 
					"smile.png"
			)
		);
		
		// test null input.
		try {
			BitmapFactory.decodeAsset(
				this.getContext(), 
				"smile.png", 
				null
			);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException iae) {
			assertTrue(true);
		}
	} // method
} // class
