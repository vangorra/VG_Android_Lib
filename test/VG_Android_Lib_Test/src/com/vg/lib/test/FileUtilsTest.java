package com.vg.lib.test;

import java.io.File;

import android.test.AndroidTestCase;

import com.vg.lib.util.FileUtils;

public class FileUtilsTest extends AndroidTestCase {
	File baseDir;
	File[] dirs;
	File[] files;
	
	private void clear() {
		for(int i = files.length - 1; i >= 0; --i) {
			if(files[i].exists()) {
				files[i].delete();
			}
		}
		
		for(int i = dirs.length - 1; i >= 0; --i) {
			if(dirs[i].exists()) {
				dirs[i].delete();
			}
		}
		
		if(baseDir.exists()) {
			baseDir.delete();
		}
	}
	
	public void setUp() {
		baseDir = new File(getContext().getFilesDir(), "base_test_dir");
		
		dirs = new File[]{
			new File(baseDir, "dir1"),
			new File(baseDir, "dir1/dir2"),
		};
		
		files = new File[] {
			new File(baseDir, "dir1/file1.txt"),
			new File(baseDir, "dir1/file2.txt"),
		};
		
		clear();
		
		for(File dir: dirs) {
			if(!dir.exists()) {
				dir.mkdir();
			}
		}
	}
	
	public void tearDown() {
		clear();
	}
	
	public void testDeleteRecursive() {
		try {
			FileUtils.deleteRecursive(null);
			assertTrue(false);
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		
		assertTrue(FileUtils.deleteRecursive(new File("/sdf/sdfs/sdfs/ffdsrehg/wefs/")));
		assertTrue(FileUtils.deleteRecursive(baseDir));
		assertFalse(baseDir.exists());
	}
}
