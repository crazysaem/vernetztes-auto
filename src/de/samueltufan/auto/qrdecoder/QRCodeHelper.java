package de.samueltufan.auto.qrdecoder;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

public class QRCodeHelper
{
	public static int imgCount = 0;
	
	public static QrCodeInfo decodeQRTag(byte[] b)
	{
		imgCount++;
		
		QrCodeInfo qrCodeInfo = new QrCodeInfo();
		
		InputStream in = new ByteArrayInputStream(b);
		BufferedImage image = null;
		
		try
		{
			image = ImageIO.read(in);
			ImageIO.write(image, "jpg", new File("src/upload/" + imgCount + ".jpg"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		qrCodeInfo = decodeQrCode(image);
		
		if (qrCodeInfo == null)
		{
			image = LocateQrCode.locate(image, "" + imgCount);
			
			if (image != null)
			{		
				//BufferedImageUtils.writeBufferedImage(image, "src/upload/" + imgCount + "crop.jpg");
		
				qrCodeInfo = decodeQrCode(image);
			}
		}
		
		return qrCodeInfo;
	}
	
	private static QrCodeInfo decodeQrCode(BufferedImage image)
	{
		QrCodeInfo qrCodeInfo = new QrCodeInfo();
		
		// convert the image to a binary bitmap source
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

		// decode the barcode
		QRCodeReader reader = new QRCodeReader();

		Result result = null;

		Map<DecodeHintType, ?> hints = new HashMap<DecodeHintType, Object>();
		hints.put(DecodeHintType.TRY_HARDER, null);			

		try
		{
			result = reader.decode(bitmap, hints);
		}
		catch (ReaderException e)
		{}

		// byte[] b = result.getRawBytes();
		if (result != null)
		{
			qrCodeInfo.value = result.getText();
			qrCodeInfo.width = getWidthFromResultPoints(result.getResultPoints());
			qrCodeInfo.height = getHeightFromResultPoints(result.getResultPoints());
			
			return qrCodeInfo;
		}
		else
		{
			return null;
		}
	}
	
	private static int getWidthFromResultPoints(ResultPoint[] points)
	{
		int xmin = 999999, xmax = -999999;
		
		for (int i=0; i<points.length; i++)
		{
			xmin = (int) Math.min(points[i].getX(), xmin);
			xmax = (int) Math.max(points[i].getX(), xmax);
		}
		
		return xmax - xmin;
	}
	
	private static int getHeightFromResultPoints(ResultPoint[] points)
	{
		int ymin = 999999, ymax = -999999;
		
		for (int i=0; i<points.length; i++)
		{
			ymin = (int) Math.min(points[i].getY(), ymin);
			ymax = (int) Math.max(points[i].getY(), ymax);
		}
		
		return ymax - ymin;
	}
}
