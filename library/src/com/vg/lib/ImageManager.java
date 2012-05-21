package com.vg.lib;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageManager {
	private static final String TAG = ImageManager.class.getName();
	
	public static Drawable getResourceDrawable(Context c, int resId) {
		return c.getApplicationContext().getResources().getDrawable(resId);
	}
	
	public static BitmapDrawable getAssetDrawable(Context c, String path) {
		try {
			return new BitmapDrawable(
					c.getApplicationContext().getResources(), 
					c.getApplicationContext().getAssets().open(path)
			);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*public static Drawable getAssetBitmap(Context c, String path, int maxWidth, int maxHeight) {
//		Log.v(TAG, "MAX DIM:"+maxWidth+"x"+maxHeight);
		try {
			InputStream inputStream = c.getApplicationContext().getAssets().open(path);
			//BitmapFactory.decodeStream(inputStream);
			
			
			// get information about the bitmap.
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(inputStream, null, opts);
			
			float scaleAmount;
			if(opts.outWidth > opts.outHeight) {
				scaleAmount = maxWidth * 1.0f / opts.outWidth * 1.0f;
			} else {
				scaleAmount = maxHeight * 1.0f / opts.outHeight * 1.0f;
			}
			
			if(scaleAmount > 1) {
				scaleAmount = 1;
			}
			
//			Log.v(TAG, "PSA:"+scaleAmount);
			int scaleValue = (int)(100 / (scaleAmount * 100));
			
//			Log.v(TAG, "SA:"+scaleAmount);
//			Log.v(TAG, "SV:"+scaleValue);
			
			opts = new BitmapFactory.Options();
			opts.inSampleSize = scaleValue;
			return new BitmapDrawable(c.getResources(), BitmapFactory.decodeStream(inputStream, null, opts));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}*/
	
	public static Bitmap bitmapFromURL(String url) {
		try{
		    URL ulrn = new URL(url);
		    HttpURLConnection con = (HttpURLConnection)ulrn.openConnection();
		    InputStream is = con.getInputStream();
		    return BitmapFactory.decodeStream(is);
		    
	    } catch(Exception e) {
	    	
	    }
		
		return null;
	}
	
	public static final SampledBitmapDrawable loadAsset(Context c, String assetPath) {
		InputStream is = null;
		try {
			is = c.getApplicationContext().getAssets().open(assetPath);
			return new SampledBitmapDrawable(
					is, 
					true
			);
		} catch (IOException e) {
			// ignore
		}
		return null;
	}
	
	public static class SampledBitmapDrawable extends Drawable {
		private InputStream inputStream;
		boolean dimensionChanged = true;
		boolean preserveAspect = true;
		private Rect bounds;
		private Bitmap bitmap;

		public SampledBitmapDrawable(InputStream is, boolean preserveAspect) {
			this.inputStream = is;
			this.preserveAspect = preserveAspect;
		}

		public void setPreserveAspect(boolean b) {
			this.preserveAspect = b;
		}
		
		public boolean getPreserveAspect() {
			return this.preserveAspect;
		}
		
		@Override
		public void setBounds(int left, int top, int right, int bottom) {
			Rect origBounds = getBounds();
			super.setBounds(left, top, right, bottom);
			this.bounds = getBounds();
			
			if(origBounds == null || origBounds.width() != bounds.width() || origBounds.height() != bounds.height()) {
				this.dimensionChanged = true;
			}
		}

		@Override
		public void setBounds(Rect bounds) {
			Rect origBounds = getBounds();
			super.setBounds(bounds);
			this.bounds = bounds;
			
			if(origBounds == null || origBounds.width() != bounds.width() || origBounds.height() != bounds.height()) {
				this.dimensionChanged = true;
			}
		}
		
		@Override
		public void draw(Canvas canvas) {
			if(dimensionChanged) {
				dimensionChanged = false;
				bitmap = readBitmap();
			}
			canvas.drawBitmap(bitmap, null, bounds, new Paint());
		}
		
		private Bitmap readBitmap() {
			int maxWidth = bounds.width();
			int maxHeight = bounds.height();
			
			// get information about the bitmap.
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(inputStream, null, opts);
			
			float scaleAmount;
			if(opts.outWidth > opts.outHeight) {
				scaleAmount = maxWidth * 1.0f / opts.outWidth * 1.0f;
			} else {
				scaleAmount = maxHeight * 1.0f / opts.outHeight * 1.0f;
			}
			
			if(scaleAmount > 1) {
				scaleAmount = 1;
			}
			
//				Log.v(TAG, "PSA:"+scaleAmount);
			int scaleValue = (int)(100 / (scaleAmount * 100));
			
//				Log.v(TAG, "SA:"+scaleAmount);
//				Log.v(TAG, "SV:"+scaleValue);
			
			opts = new BitmapFactory.Options();
			opts.inSampleSize = scaleValue;
			
			if(!this.preserveAspect) {
				opts.outWidth = bounds.width();
				opts.outHeight = bounds.height();
			}
			
			return BitmapFactory.decodeStream(inputStream, null, opts);
		}

		@Override
		public int getOpacity() {
			return 0;
		}

		@Override
		public void setAlpha(int alpha) {
			
		}

		@Override
		public void setColorFilter(ColorFilter cf) {
			
		}
	}
}
