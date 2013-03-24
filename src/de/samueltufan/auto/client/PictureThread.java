package de.samueltufan.auto.client;

import java.io.File;
import java.io.IOException;

import de.samueltufan.auto.qrdecoder.QrCodeInfo;
import de.samueltufan.auto.server.ServerInterface;


public class PictureThread implements Runnable
{
	private ServerInterface server;
	private static int count = 0;
	
	public PictureThread(ServerInterface server)
	{
		this.server = server;
	}
	
	@Override
	public void run()
	{		
		while(true)
		{
			//Wait 50 ms
			try 
			{
	            Thread.sleep(50);
	        } 
			catch (InterruptedException ie) 
	        {
	            System.out.println("PictureThread interrupted! " + ie);
	        }
			
			Runtime rt = Runtime.getRuntime();		
			try
			{
				System.out.println("Capturing picture...");
				Process pr = rt.exec("fswebcam -r 1920x1080 output.jpg");
				pr.waitFor();
				System.out.println("Picture captured");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			File f = new File("output.jpg");
			
			if (f.exists()) 
			{
				try
				{
					System.out.println("Converting picture");
					byte[] b = FileHelper.loadFile(f);
					//f.delete();
					System.out.println("Sending picture to server to decode it");
					String[] result = server.decodeQrCode(b);
					
					if (result != null)
					{
						QrCodeInfo qrCodeInfo = new QrCodeInfo();
						qrCodeInfo.value = result[0];
						qrCodeInfo.width = Integer.parseInt(result[1]);
						qrCodeInfo.height = Integer.parseInt(result[2]);						
						
						System.out.println(count + " v:" + qrCodeInfo.value + " w:" + qrCodeInfo.width + " h:" + qrCodeInfo.height);
					}
					else
					{
						System.out.println(count + " DECODING ERROR");
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			count++;
		}
	}
}
