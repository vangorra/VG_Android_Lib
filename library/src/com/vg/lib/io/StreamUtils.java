package com.vg.lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
	
	/**
	 * Copy and input stream to the specified output stream using a 1024 byte buffer size.
	 * @param in Stream to copy from
	 * @param out Stream to copy to
	 * @return True if the copy was successful, false otherwise.
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream in, OutputStream out) throws IOException {
	    return copyStream(in, out, 1024);
	}
	
	/**
	 * Copy and input stream to the specified output stream using a provided buffer size.
	 * @param in Stream to copy from
	 * @param out Stream to copy to
	 * @return True if the copy was successful, false otherwise.
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream in, OutputStream out, int bufferSize) throws IOException {
		byte[] buffer = new byte[bufferSize];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	    
	    return true;
	}
}
