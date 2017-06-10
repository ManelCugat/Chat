package manel.sockets;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Cliente {

	public static void main(String[] args) {
		
		
		MarcoCliente marcoCliente=new MarcoCliente();
		
		marcoCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

}


class MarcoCliente extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public MarcoCliente(){
		
		this.setBounds(300, 300, 400, 450);
		
		this.setTitle("Cliente");
		
		LaminaCliente laminaCliente=new LaminaCliente();
		
		this.add(laminaCliente);
		
		this.setVisible(true);
		
	}
	
	
}


class LaminaCliente extends JPanel{
	
	
	private static final long serialVersionUID = 1L;

	public LaminaCliente(){
		
		
		nick=new JTextField(10 );
	
		JLabel etiqueta=new JLabel("--CHAT--", JLabel.CENTER);
		
		ip = new JTextField(10);
	
		textoEnvio =new JTextField (30);
	
		campoChat=new JTextArea (20,30);
		
		botonEnvio=new JButton("Enviar");
		
		botonEnvio.addActionListener(new EnviaTexto());
		
		this.add(nick);
		
		this.add(etiqueta);
		
		this.add(ip);
		
		this.add(campoChat);
		
		this.add(textoEnvio);
		
		this.add(botonEnvio);
	
	}
	
	private class EnviaTexto implements ActionListener{


		public void actionPerformed(ActionEvent e) {


			try {
				
				miSocket=new Socket("192.168.1.99",9999);
				
				PaqueteEnvio datos=new PaqueteEnvio();
						
				datos.setNick(nick.getText());
				
				datos.setIp(ip.getText());
				
				datos.setMensaje(textoEnvio.getText());
				
				ObjectOutputStream paquete_envio = new ObjectOutputStream (miSocket.getOutputStream());
				
				paquete_envio.writeObject(datos);

				miSocket.close();
				
			} catch (IOException e1) {

				System.out.println("-----------Problemas creando Socket-------------- \n "+e1);
			}
		}
		
	Socket miSocket;	
		
	}
	
	JTextField textoEnvio, nick, ip;
	JButton botonEnvio;
	private JTextArea campoChat;
	
}


class PaqueteEnvio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nick, ip, mensaje;
	
	public PaqueteEnvio(){}
	
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	

	
	
	
}
