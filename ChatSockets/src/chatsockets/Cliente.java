package chatsockets;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;
import java.util.*;

public class Cliente extends JFrame implements ActionListener
{
	static Socket sfd = null;
	static DataInputStream EntradaSocket;
	static DataOutputStream SalidaSocket;
	static JTextArea entrada;
	private JTextField salida;
	private JButton btnEnviar;
	private JPanel pnlEnviar;
	private JFrame frmUsusarios;
	private JList lstUsuarios;
	private JMenuBar barMenu;
	private JMenu mnuInicio,mnuAcerca;
	private JMenuItem itmUsuarios, itmSalir, itmPrivado,itmAcerca;
	private JScrollPane sp;
	
	String texto;
	
	public Cliente()
	{
		estilo();
		
		setTitle("Chat");
		setBounds(330,125,350,200);
		
		itmUsuarios = new JMenuItem("Ver usuarios");
		itmUsuarios.setActionCommand("ver");
		itmUsuarios.addActionListener(this);
		
		itmSalir = new JMenuItem("Desconectar");
		itmSalir.setActionCommand("salir");
		itmSalir.addActionListener(this);
		
		itmPrivado = new JMenuItem("Privado con: ");
		itmPrivado.setActionCommand("privado");
		itmPrivado.addActionListener(this);
		
		mnuInicio = new JMenu ("Iniciar aqui ");
		mnuInicio.add(itmUsuarios);
		mnuInicio.add(itmPrivado);
		mnuInicio.addSeparator();
		mnuInicio.add(itmSalir);
		
		itmAcerca = new JMenuItem("Acerca");
		itmAcerca.setActionCommand("acerca");
		itmAcerca.addActionListener(this);		
		
		mnuAcerca = new JMenu("Ayuda");
		mnuAcerca.add(itmAcerca);
			
		barMenu = new JMenuBar();
		barMenu.add(mnuInicio);
		barMenu.add(mnuAcerca);
		
		setJMenuBar(barMenu);
				
		salida = new JTextField(30);
		salida.addActionListener(this); 

		entrada = new JTextArea();
		entrada.setBackground(new Color(240,240,245));
		entrada.setEditable(false); 
		
		sp = new JScrollPane(entrada);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setPreferredSize(new Dimension(450, 450));
		
		btnEnviar = new JButton ("Enviar");
		btnEnviar.setActionCommand("enviar");
		btnEnviar.addActionListener(this);
		
		lstUsuarios = new JList();
		lstUsuarios.setBackground(new Color(0,0,0));
		
		pnlEnviar = new JPanel();
		pnlEnviar.setLayout(new BorderLayout());
		pnlEnviar.add(salida, BorderLayout.CENTER);
		pnlEnviar.add(btnEnviar, BorderLayout.EAST);
				
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(pnlEnviar, BorderLayout.SOUTH);
		getContentPane().add(sp, BorderLayout.CENTER);
		

		addWindowListener(new WindowAdapter() 
		{
            public void windowClosing(WindowEvent e) 
            {
            	salir();
            }
        });

    	setVisible(true);
    }
    
    public static void estilo()
    {
    	try
    	{
    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	}
    	catch(Exception e)
    	{}
    }    
    
    public static void main(String[] args)
	{ 		
		try
		{
			sfd = new Socket("localhost",7000);
			EntradaSocket = new DataInputStream(new BufferedInputStream(sfd.getInputStream()));
			SalidaSocket = new DataOutputStream(new BufferedOutputStream(sfd.getOutputStream()));
		}
		
		catch (UnknownHostException uhe)
		{
			System.out.println("No se puede acceder al servidor.");
			System.exit(1);
		}
		
		catch (IOException ioe)
		{
			JOptionPane.showMessageDialog(null, "No existe un servidor\nIntenta m� tarde","Error", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
		
		Cliente cliente = new Cliente();
		
		while (true)
		{
			try
			{
				String linea = EntradaSocket.readUTF();
				entrada.append(linea+"\n");
			}
			catch(IOException ioe)
			{
				System.exit(1);
			}
		}
	}
	
	public void actionPerformed (ActionEvent e)
	{
		texto = salida.getText();
		salida.setText("");
		
		if(e.getActionCommand() == "enviar")
		{	try
			{
				SalidaSocket.writeUTF(texto);
				SalidaSocket.flush();
				
			}
			catch (IOException ioe)
			{
				System.out.println("Error: "+ioe);
			}
		}
		else
		{		try
			{
				SalidaSocket.writeUTF(texto);
				SalidaSocket.flush();
			}
			catch (IOException ioe)
			{
				System.out.println("Error: "+ioe);
			}
		}
		if(e.getActionCommand() == "ver")
		{	
			System.out.println("Estos son los usuarios");
		}
		if(e.getActionCommand() == "salir")
		{	
			salir();
		}
		
		if(e.getActionCommand() == "acerca")
		{
			acerca();
		}
			
	}
	public void salir()
	{
		acerca();
		System.exit(0);
	}
	public void acerca()
	{
		JOptionPane.showMessageDialog(null, "Edgar Leonardo Orejuela Morales\nUniversidad Distrital FJ de Caldas\nxleorzicks@yahoo.com\nBogot� Colombia","Creado por: ",JOptionPane.INFORMATION_MESSAGE);
	}
	/*
	public boolean handleEvent(Event e)
	{
		if ((e.target == this) && (e.id == Event.WINDOW_DESTROY))
		{
			if (sfd != null)
			{
				try
				{
					sfd.close();
				}
				catch (IOException ioe)
				{
					System.out.println("Error: "+ioe);
				}
				this.dispose();
			}
		}
		return true;
	}
	*/
}
