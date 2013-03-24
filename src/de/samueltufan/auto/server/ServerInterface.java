package de.samueltufan.auto.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote
{
	public String sayHello() throws RemoteException;
	public void uploadFile(byte[] b) throws RemoteException;
	public String[] decodeQrCode(byte[] b) throws RemoteException;
}
