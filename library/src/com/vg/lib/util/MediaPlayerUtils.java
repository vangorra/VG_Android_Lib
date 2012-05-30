package com.vg.lib.util;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public final class MediaPlayerUtils {
	/**
	 * Sets a media player to release after it completes.
	 * @param mp
	 */
	public static void setAutoRelease(MediaPlayer mp) {
		// set the listener and create it.
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			} // method
		});
	} // method
} // class
