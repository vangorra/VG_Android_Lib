package com.vg.lib.media;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

public class MediaPlayer extends android.media.MediaPlayer {
	/**
	 * Convenience method to create a MediaPlayer for a given asset. On success, prepare() will already have been called and must not be called again.
	 * When done with the MediaPlayer, you should call release(), to free the resources. If not released, too many MediaPlayer instances will result in an exception.
	 * @param context the Context to use
	 * @param assetFile relative path of the asset file. eg img/place.png, place.png
	 * @return a MediaPlayer or null if creation failed.
	 */
	public static MediaPlayer createFromAsset(Context context, String assetFile) {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(assetFile == null) {
			throw new IllegalArgumentException("assetFile cannot be null");
		}
		
		try {
			// get information about the asset.
			AssetFileDescriptor afd = context.getAssets().openFd(assetFile);
			
			// init media player and set data source.
			MediaPlayer mp = new MediaPlayer();
			mp.setDataSource(
					afd.getFileDescriptor(), 
					afd.getStartOffset(), 
					afd.getLength()
			);
			// preage the media player.
			mp.prepare();
			
			// close the asset file descriptor.
			afd.close();
			
			// return the new media player.
			return mp;
			
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (IllegalStateException e) {
			// ignore
		} catch (IOException e) {
			// ignore
		}
		
		return null;
	} // method
} // class
