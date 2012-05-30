package com.vg.lib.util;

import java.io.File;
import java.io.FileNotFoundException;

public class FileUtils {
    /**
     * Delete files recursively. 
     * @param path Path of the file/dir to delete.
     * @return true if the file and all sub files/directories have been removed, false otherwise.
     */
	public static boolean deleteRecursive(File path){
		if(path == null) {
			throw new IllegalArgumentException("path cannot be null");
		}
		
		if(!path.exists()) {
			return true;
		}
			
		_deleteRecursive(path);
		return path.exists();
	}
	
    private static boolean _deleteRecursive(File path){
        boolean ret = true;
        if (path.isDirectory()){
            for (File f : path.listFiles()){
                ret = ret && FileUtils.deleteRecursive(f);
            }
        }
        return ret && path.delete();
    } // method
} // class