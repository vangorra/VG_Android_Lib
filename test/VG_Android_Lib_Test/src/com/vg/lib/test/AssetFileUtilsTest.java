package com.vg.lib.test;

import java.io.File;
import java.io.IOException;

import com.vg.lib.util.AssetFileUtils;

import android.test.AndroidTestCase;

public class AssetFileUtilsTest extends AndroidTestCase {
	File destPath;
	
	public void setUp() {
		destPath = new File(
			getContext().getFilesDir(),
			"mysmile.png"
		);
		
		if(destPath.exists()) {
			destPath.delete();
		}
	}
	
	public void tearDown() {
		if(destPath.exists()) {
			destPath.delete();
		}
	}
	
	public void testCopyFileIfChanged() {
		try {
			AssetFileUtils.copyFileIfChanged(null, null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFileIfChanged(getContext(), null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFileIfChanged(null, "", null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFileIfChanged(getContext(), "smile.png", null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFileIfChanged(
					getContext(), 
					"not_here.txt", 
					destPath.getAbsolutePath()
			);
			assertTrue(false);
		} catch (IOException e) {
			assertTrue(true);
		}
		
		try {
			assertFalse(AssetFileUtils.copyFileIfChanged(
					getContext(), 
					"smile.png", 
					"/usr/not_here.txt"
			));
		} catch (IOException e) {
			assertTrue("Problem getting the src file", false);
		}

		try {
			assertTrue(AssetFileUtils.copyFileIfChanged(
					getContext(), 
					"smile.png", 
					destPath.getAbsolutePath()
			));
		} catch (IOException e) {
			assertTrue("Problem getting the source file.", false);
		}
		
		assertTrue(destPath.exists());
		
		try {
			assertTrue(AssetFileUtils.copyFileIfChanged(
					getContext(), 
					"smile.png", 
					destPath.getAbsolutePath()
			));
		} catch (IOException e) {
			assertTrue("Problem getting the source file.", false);
		}
		
		try {
			assertTrue(AssetFileUtils.copyFileIfChanged(
					getContext(), 
					"audio.mp3", 
					destPath.getAbsolutePath()
			));
		} catch (IOException e) {
			assertTrue("Problem getting the source file.", false);
		}
	} // method
	
	public void testCopyFile() {
		try {
			AssetFileUtils.copyFile(null, null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFile(getContext(), null, null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFile(null, "", null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFile(getContext(), "smile.png", null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		} catch (IOException io) {
			assertTrue(false);
		}
		
		try {
			AssetFileUtils.copyFile(
					getContext(), 
					"not_here.txt", 
					destPath.getAbsolutePath()
			);
			assertTrue(false);
		} catch (IOException e) {
			assertTrue(true);
		}
		
		try {
			assertFalse(AssetFileUtils.copyFile(
					getContext(), 
					"smile.png", 
					"/usr/not_here.txt"
			));
		} catch (IOException e) {
			assertTrue("Problem getting the src file", false);
		}

		try {
			assertTrue(AssetFileUtils.copyFile(
					getContext(), 
					"smile.png", 
					destPath.getAbsolutePath()
			));
		} catch (IOException e) {
			assertTrue("Problem getting the source file.", false);
		}
		
		assertTrue(destPath.exists());
	} // method
} // class
