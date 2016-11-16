package chatsockets;

import java.io.*;
import java.net.*;
import java.util.*; 
import javax.swing.*;
import java.awt.*;

public class Servidor extends Thread
{
	public static Vector usuarios = new Vector();
	
	public void Servidor()
	{}
	
	public static void main (String args[])
	{
		Servidor s = new Servidor();
						
		ServerSocket sfd = null;
		
		try
		{
			sfd = new ServerSocket(7000);
		}
		
		catch (IOException ioe)
		{
			System.out.println("Comunicaci�n rechazada."+ioe);
			System.exit(1);
		}
		
		while (true)
		{
			try
			{
				Socket nsfd = sfd.accept();
				System.out.println("Conexion aceptada de: "+nsfd.getInetAddress());
				Flujo flujo = new Flujo(nsfd);
				Thread t = new Thread(flujo);
				t.start();
			}
			catch(IOException ioe)
			{
				System.out.println("Error: "+ioe);
			}
		}
	}
} 