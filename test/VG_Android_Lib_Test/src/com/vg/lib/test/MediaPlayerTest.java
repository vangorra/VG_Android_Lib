package com.vg.lib.test;

import android.media.MediaPlayer.OnCompletionListener;
import android.test.AndroidTestCase;

import com.vg.lib.media.MediaPlayer;
import com.vg.lib.util.MediaPlayerUtils;

public class MediaPlayerTest extends AndroidTestCase {
	public void testCreateFromAsset() {
		MediaPlayer mp;
		
		// test null input.
		try {
			mp = MediaPlayer.createFromAsset(null, null);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException iae) {
			assertTrue(true);
		}
		
		// test null input.
		try {
			mp = MediaPlayer.createFromAsset(getContext(), null);
			assertTrue("Method should throw exception.", false);
		} catch (IllegalArgumentException iae) {
			assertTrue(true);
		}

		// create empty
		mp = MediaPlayer.createFromAsset(getContext(), "");
		assertNull("Media player should fail to create", mp);
		
		// create and play
		mp = MediaPlayer.createFromAsset(getContext(), "audio.mp3");
		assertNotNull("Failed to create media player.", mp);
		mp.start();
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(android.media.MediaPlayer mp) {
				mp.release();
			}
		});
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// ignore
		}
		
		// create, play, set auto release
		mp = MediaPlayer.createFromAsset(getContext(), "audio.mp3");
		MediaPlayerUtils.setAutoRelease(mp);
		mp.start();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// ignore
		}
	} // method
} // class
