package com.vg.lib.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

public class AssetFileUtils {
	/**
	 * Copy an asset file to a place on the local filesystem (usually the app's home directory) 
	 * only if the source and destination files are different. Change comparison is done based on
	 * size (in bytes) of each file. Not perfect, but fast. 
	 * @param context The activity context.
	 * @param assetPath relative path of the asset file. eg img/place.png, place.png
	 * @param destPath absolute path of the destination file.
	 * @return true is the copy was successful, false otherwise.
	 * @throws IOException
	 */
	public static boolean copyFileIfChanged(Context context, String assetPath, String destPath) throws IOException {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(assetPath == null) {
			throw new IllegalArgumentException("assetPath cannot be null");
		}
		
		// check for empty
		if(assetPath.length() == 0) {
			throw new IllegalArgumentException("assetPath cannot be an empty string.");
		}
		
		// check for null
		if(destPath == null) {
			throw new IllegalArgumentException("destPath cannot be null");
		}
		
		// init
		AssetManager assetManager = context.getAssets();
		File srcFile = new File(assetPath);
		File destFile = new File(destPath);
		long srcFileLength = -1;
		long destFileLength = -1;
		
		// make the dest file an actual file.
		if(destFile.isDirectory()) {
			destFile = new File(destFile, srcFile.getName());
		}
		
		// get the length of the asset file.
		AssetFileDescriptor desc = assetManager.openFd(assetPath);
		srcFileLength = desc.getLength();
		desc.close();
		
		// get the length of the dest file.
		if(destFile.exists()) {
			destFileLength = destFile.length();
		}
		
		// if the file lengths are the same, return, nothing has changed.
		if(srcFileLength == destFileLength) {
			return true;
		}
		
		// delete the old destfile, we are about to write it.
		if(destFile.exists()) {
			destFile.delete();
		}
		
		// copy data from the asset to the dest file.
		InputStream is = assetManager.open(assetPath);
		try {
			OutputStream os = new FileOutputStream(destFile);
			copyStream(is, os);
			is.close();
			os.close();
			return true;
		} catch (IOException io) {
			is.close();
			return false;
		}
	} // method
	
	/**
	 * Copy an asset file to a place on the local filesystem. (usually the app's home directory)
	 * @param context The activity context.
	 * @param assetPath relative path of the asset file. eg img/place.png, place.png
	 * @param destPath absolute path of the destination file.
	 * @return true is the copy was successful, false otherwise.
	 * @throws IOException
	 */
	public static boolean copyFile(Context context, String assetPath, String destPath) throws IOException {
		// check for null
		if(context == null) {
			throw new IllegalArgumentException("context cannot be null");
		}
		
		// check for null
		if(assetPath == null) {
			throw new IllegalArgumentException("assetPath cannot be null");
		}
		
		// check for empty
		if(assetPath.length() == 0) {
			throw new IllegalArgumentException("assetPath cannot be an empty string.");
		}
		
		// check for null
		if(destPath == null) {
			throw new IllegalArgumentException("destPath cannot be null");
		}
		
		// init
		AssetManager assetManager = context.getAssets();
		File srcFile = new File(assetPath);
		File destFile = new File(destPath);
		
		// make the dest file an actual file.
		if(destFile.isDirectory()) {
			destFile = new File(destFile, srcFile.getName());
		}
		
		// delete the old destfile, we are about to write it.
		if(destFile.exists()) {
			destFile.delete();
		}
		
		// copy data from the asset to the dest file.
		InputStream is = assetManager.open(assetPath);
		try {
			OutputStream os = new FileOutputStream(destFile);
			copyStream(is, os);
			is.close();
			os.close();
			return true;
		} catch (IOException io) {
			is.close();
			return false;
		}
	} // method
	
	/**
	 * Copy an InputStream to an OutputStream
	 * @param in
	 * @param out
	 * @throws IOException
	 */
	private static void copyStream(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
}
