package com.vg.lib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.util.Log;

public class AssetFileCopier {
	
	private static long splitFileSize(Context c, String[] strokeOrderFontAssetFiles) {
		long size = 0;
		String[] files = strokeOrderFontAssetFiles;
		try {
			for(int i = 0; i < files.length; i++) {
				InputStream is = c.getAssets().open(files[i]);
				size += is.available();
		    	is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}

		return size;
	}
	
	public static void copySplitFile(Context context, String[] assetFilePaths, String destPath, boolean force) {
		File destFile = new File(destPath);
		boolean shouldConcatFiles = false;
		
		if(force) {
			shouldConcatFiles = true;
		}
		
//		Log.v("TEST", "Copy Files to:"+destFile.getAbsolutePath());
		if(!destFile.exists()) {
			shouldConcatFiles = true;
		} else {
			long concatSize = splitFileSize(context, assetFilePaths);
			if(concatSize != destFile.length()) {
				shouldConcatFiles = true;
			}
		}
		
		if(!shouldConcatFiles) {
//			Log.v("TEST", "wont copy file.");
			return;
		}
		
		destFile.delete();
//		Log.v("TEST", "Copy file");
		destFile.getParentFile().mkdirs();
		try {
			OutputStream os = new FileOutputStream(destFile);

			for(int i = 0; i < assetFilePaths.length; i++) {
					InputStream is = context.getAssets().open(assetFilePaths[i]);
					byte[] buffer = new byte[1024];
			    	int length;
			    	while ((length = is.read(buffer))>0){
			    		os.write(buffer, 0, length);
			    	}
			    	is.close();
			}

			os.flush();
	    	os.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}

//		Log.v("TEST", "Done.");
		return;
	}
	
	public static boolean copyFile(Context context, String assetPath, String destPath, boolean force) {
		File srcFile = new File(assetPath);
		File destFile = new File(destPath);
		boolean shouldCopyFile = false;
		
		if(destFile.isDirectory()) {
			destFile = new File(destFile, srcFile.getName());
		}
		
//		Log.v("TEST", "copy:"+assetPath +" -> "+ destFile.getAbsolutePath());
		if(!srcFile.exists()) {
			shouldCopyFile = true;
		}
		
		if(force) {
			shouldCopyFile = true;
		}
		
		if(!destFile.exists()) {
			shouldCopyFile = true;
		} else {
			if(srcFile.length() != destFile.length()) {
				shouldCopyFile = true;
			}
		}
		
		if(!shouldCopyFile) {
//			Log.v("TEST", "Wont copy");
			return false;
		}
		
		destFile.delete();
//		Log.v("TEST", "Copying");
		destFile.getParentFile().mkdirs();
		try {
			OutputStream os = new FileOutputStream(destFile);
			long progress = 0;

			InputStream is = context.getAssets().open(assetPath);
			byte[] buffer = new byte[1024];
	    	int length;
	    	while ((length = is.read(buffer))>0){
	    		os.write(buffer, 0, length);
	    		progress += length;
	    		//installProgress = (int)(progress / fontFileSize * 100);
	    	}
	    	is.close();

			os.flush();
	    	os.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

//		Log.v("TEST", "Done");
		return true;
	}
}
