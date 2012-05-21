package com.vg.lib;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

public class AudioManager {
	private static final String TAG = AudioManager.class.getName();
	
	public static void playAsset(Context c, String path) {
		try {
			AssetFileDescriptor afd = c.getApplicationContext().getResources().getAssets().openFd(path);
			MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mp.prepare();
			
			afd.close();
			
			if(mp != null) {
				mp.start();
				mp.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						mp.release();
					}
				});
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
