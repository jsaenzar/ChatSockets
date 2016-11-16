package chatsockets;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.*; 

public class Flujo extends Thread
{
	Socket nsfd;
	DataInputStream FlujoLectura;
	DataOutputStream FlujoEscritura; 

	public Flujo (Socket sfd)
	{
		nsfd = sfd;
		try
		{
			FlujoLectura = new DataInputStream(new BufferedInputStream(sfd.getInputStream()));
			FlujoEscritura = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
		}
		catch(IOException ioe)
		{
			System.out.println("IOException(Flujo): "+ioe);
		}
	}
	
	public void run()
	{
		Cliente.estilo();
		
		String usuario = JOptionPane.showInputDialog("Ingresa tu NicK: ");
		broadcast("(" + usuario + " se ha conectado)\n");
		Servidor.usuarios.add ((Object) this);
		
		while(true)
		{
			try
			{
				
				String linea = FlujoLectura.readUTF();
				if (!linea.equals(""))
				{
					linea = usuario + " dice: \n\t" + linea + "\n";
					broadcast(linea);
				}
			}
			catch(IOException ioe)
			{
				Servidor.usuarios.removeElement(this);
				broadcast("(" + usuario + " se ha desconectado)\n");
				break;
			}
		}
	}
	
	public void broadcast(String mensaje)
	{
		synchronized (Servidor.usuarios)
		{
			Enumeration e = Servidor.usuarios.elements();
			while (e.hasMoreElements())
			{
				Flujo f = (Flujo) e.nextElement();
				try
				{
					synchronized(f.FlujoEscritura)
					{
						f.FlujoEscritura.writeUTF(mensaje);
						f.FlujoEscritura.flush();
					}
				}
				catch(IOException ioe)
				{
					System.out.println("Error: "+ioe);
				}
			}
		}
	}
}

