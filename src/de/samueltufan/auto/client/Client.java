package de.samueltufan.auto.client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;

import de.samueltufan.auto.server.ServerInterface;

public class Client
{
	public static void main(String[] args) throws Exception 
	{        
		String ip = readFile("src/server-ip.txt");
		
        String url = "rmi://" + ip + "/server";
        ServerInterface server = (ServerInterface) Naming.lookup(url);
        System.out.println("Server says: " + server.sayHello());
        
        /*File f = new File("src/data/test.jpg");
        byte[] b = FileHelper.loadFile(f);
        server.uploadFile(b);*/
        
        Thread pictureThread = new Thread(new PictureThread(server));        
        pictureThread.start();
    }
	
	private static String readFile( String file ) throws IOException 
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
}
