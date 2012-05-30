package com.vg.lib.graphics;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

/**
 * Creates Bitmap objects from various sources, including files, streams, byte-arrays, urls and assets.
 * @author vangorra
 *
 */
public class BitmapFactory extends android.graphics.BitmapFactory {
	/**
	 * Decode an image URL into a bitmap.
	 * @param url The http url of the image.
	 * @return The decoded bitmap or null if the image data could not be decoded.
	 */
	public static Bitmap decodeURL(URL url) {
		// check for null
		if(url == null) {
			throw new IllegalArgumentException("url cannot be null");
		}
		
		// return the bitmap
		return decodeURL(url, new Options());
	} // method
	
	/**
	 * Decode an image URL into a bitmap.
	 * @param url The http url of the image.
	 * @param opts The options for decoding the image.
	 * @return The decoded bitmap or null if the image data could not be decoded.
	 */
	public static Bitmap decodeURL(URL url, Options opts) {
		// check for null
		if(url == null) {
			throw new IllegalArgumentException("url cannot be null");
		}
		
		// check for null
		if(opts == null) {
			throw new IllegalArgumentException("opts cannot be null");
		}
		
		try{
			// open a connection to the image.
		    HttpURLConnection con = (HttpURLConnection)url.openConnection();
		    InputStream is = con.getInputStream();
		    
		    // decode the image
		    Bitmap b = BitmapFactory.decodeStream(is, null, opts);
		    
		    // close stream and return the bitmap.
		    is.close();
		    return b;
		
		// image could not be decoded.
	    } catch(Exception e) {
	    	return null;
	    }
	} // method
	
	/**
	 * Decode an asset file into a bitmap.
	 * @param context The context to use
	 * @param assetFile assetFile relative path of the asset file. eg img/place.png, place.png
	 * @return The decoded bitmap or null if the image data could not be decoded.
	 */
	public static Bitmap decodeAsset(Context context, String assetFile) {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(assetFile == null) {
			throw new IllegalArgumentException("assetFile cannot be null");
		}
		
		// return the bitmap.
		return decodeAsset(context, assetFile, new Options());
	}
	
	/**
	 * Decode an asset file into a bitmap.
	 * @param context The context to use
	 * @param assetFile assetFile relative path of the asset file. eg img/place.png, place.png
	 * @param opts Options when constructing the bitmap.
	 * @return The decoded bitmap or null if the image data could not be decoded.
	 */
	public static Bitmap decodeAsset(Context context, String assetFile, Options opts) {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(assetFile == null) {
			throw new IllegalArgumentException("assetFile cannot be null");
		}

		// check for null
		if(opts == null) {
			throw new IllegalArgumentException("opts cannot be null");
		}
		
		// get the asset manager.
		AssetManager assetManager = context.getAssets();
		try {
			// open the stream and decode the bitmap.
	        InputStream istr = assetManager.open(assetFile);
	        Bitmap b = decodeStream(istr, null, opts);
	        
	        // close the stream and return the bitmap.
	        istr.close();
	        return b;
	        
	    // something else happened, return null.
		} catch (IOException io) {
			return null;
		}
	} // method
} // class
