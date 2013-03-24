package de.samueltufan.auto.server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.imageio.ImageIO;

import de.samueltufan.auto.qrdecoder.QRCodeHelper;
import de.samueltufan.auto.qrdecoder.QrCodeInfo;

public class Server extends UnicastRemoteObject implements ServerInterface
{
	Registry rmiRegistry;
	
	public Server() throws RemoteException 
	{
	    super();
	}
	
	public void start() throws Exception 
	{
	    rmiRegistry = LocateRegistry.createRegistry(1099);
	    rmiRegistry.bind("server", this);
	    System.out.println("Server started");
	}
	public void stop() throws Exception 
	{
	    rmiRegistry.unbind("server");
	    unexportObject(this, true);
	    unexportObject(rmiRegistry, true);
	    System.out.println("Server stopped");
	}
	
	@Override
	public String sayHello() 
	{
	    return "Hello world";
	}
	
	@Override
	public void uploadFile(byte[] b) throws RemoteException
	{
		InputStream in = new ByteArrayInputStream(b);
		BufferedImage bImageFromConvert;
		try
		{
			bImageFromConvert = ImageIO.read(in);
			ImageIO.write(bImageFromConvert, "jpg", new File("src/upload/test.jpg"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		
	}
        
    public static void main(String[] args) throws Exception 
    {
    	Server server = new Server();
        server.start();
        Thread.sleep(5 * 60 * 1000); // run for 5 minutes
        server.stop();
    }

	@Override
	public String[] decodeQrCode(byte[] b) throws RemoteException
	{
		QrCodeInfo qrCodeInfo =  QRCodeHelper.decodeQRTag(b);
		
		if (qrCodeInfo == null)
		{
			return null;
		}
		
		String[] result = new String[3];
		
		result[0] = qrCodeInfo.value;		
		result[1] = "" + qrCodeInfo.width;	
		result[2] = "" + qrCodeInfo.height;	
		
		return result;
	}	
}
