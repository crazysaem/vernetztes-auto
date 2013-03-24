package de.samueltufan.auto.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class FileHelper
{
	public static String readFile( String file ) throws IOException 
	{
	    BufferedReader reader = new BufferedReader( new FileReader (file));
	    String         line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    String         ls = System.getProperty("line.separator");

	    while( ( line = reader.readLine() ) != null ) 
	    {
	        stringBuilder.append( line );
	        stringBuilder.append( ls );
	    }

	    String result = stringBuilder.toString();
	    result = result.replaceAll("\r", "");
	    result = result.replaceAll("\n", "");
	    
	    return result;
	}
	
	public static byte[] loadFile(File file) throws IOException 
	{
	    InputStream is = new FileInputStream(file);
 
	    long length = file.length();
	    if (length > Integer.MAX_VALUE) 
	    {
	        // File is too large
	    }
	    
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) 
	    {
	        offset += numRead;
	    }
 
	    if (offset < bytes.length) 
	    {
	        throw new IOException("Could not completely read file "+file.getName());
	    }
 
	    is.close();
	    return bytes;
	}
}
