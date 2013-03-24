package de.samueltufan.auto.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.samueltufan.auto.misc.FileHelper;
import de.samueltufan.auto.server.ServerInterface;

public class Client
{
	public static void main(String[] args) throws Exception 
	{        
		String ip = FileHelper.readFile("src/server-ip.txt");
		
        //String url = "rmi://" + ip + "/server";
        //System.out.println("Connecting to \"" + url + "\"");
		//ServerInterface server = (ServerInterface) Naming.lookup(url);
        
		System.out.println("Looking up registry");
        Registry registry = LocateRegistry.getRegistry(ip, 1099);
        System.out.println("Looking up service");
        ServerInterface server = (ServerInterface) registry.lookup("server");        
        System.out.println("Testing service");
        System.out.println("Server says: " + server.sayHello());
        
        /*File f = new File("src/data/test.jpg");
        byte[] b = FileHelper.loadFile(f);
        server.uploadFile(b);*/
        
        Thread pictureThread = new Thread(new PictureThread(server));        
        pictureThread.start();
    }
	
	
}
