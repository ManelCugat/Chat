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
	
	
	Socket miSocket;
	Socket avisoSocket;
	
	private static final long serialVersionUID = 1L;

	public LaminaCliente(){
		
		nick_entrada=JOptionPane.showInputDialog("Por favor, Introduce tu nickname");
		
		nick=new JTextField(10 );
	
		JLabel etiqueta=new JLabel("--CHAT--", JLabel.CENTER);
		
		ip = new JTextField(10);
	
		textoEnvio =new JTextField (30);
	
		campoChat=new JTextArea (20,30);
		
		JScrollPane scroll = new JScrollPane(campoChat);

		botonEnvio=new JButton("Enviar");
		
		botonEnvio.addActionListener(new EnviaTexto());
		
		nick.setText(nick_entrada);
		
		nick.setEnabled(false);
		
		this.add(nick);
		
		this.add(etiqueta);
		
		this.add(ip);
		
		this.add(scroll);
		
		this.add(textoEnvio);
		
		this.add(botonEnvio);
		
		Thread t = new Thread(new RecibeTexto());
		
		t.start();
		
		avisoOnline();
		

	}

	public void avisoOnline(){
		
		ObjectOutputStream envioOnline=null;
		
		try {
			avisoSocket = new Socket("192.168.1.99",9999);
			
			PaqueteEnvio avisoOnline = new PaqueteEnvio();
			
			avisoOnline.setNick(nick_entrada);
			
			avisoOnline.setIp(null);
			
			avisoOnline.setMensaje(null);
			
			avisoOnline.setOnline(true);
			
			envioOnline=new ObjectOutputStream (avisoSocket.getOutputStream());
			
			envioOnline.writeObject(avisoOnline);
			
			envioOnline.close();
			
			avisoSocket.close();
			
		} catch (UnknownHostException e) {
			System.out.println("Problema en cliente creando avisoOnline");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Problema en cliente creando avisoOnline");
			e.printStackTrace();
		}
		
	}
	
	
	private class EnviaTexto implements ActionListener{


		public void actionPerformed(ActionEvent e) {


			try {
				
				miSocket=new Socket("192.168.1.99",9999);
			
				PaqueteEnvio datos=new PaqueteEnvio();
						
				datos.setNick(nick_entrada);
				
				datos.setIp(ip.getText());
				
				datos.setMensaje(textoEnvio.getText());
				
				datos.setOnline(true);
			
				ObjectOutputStream paquete_envio = new ObjectOutputStream (miSocket.getOutputStream());
				
				paquete_envio.writeObject(datos);
			
				paquete_envio.close();
				
				miSocket.close();
					
				campoChat.append(nick_entrada + ": " +textoEnvio.getText() + "\n");
				
				textoEnvio.setText("");
				
			} catch (IOException e1) {

				System.out.println("-----------Servidor OFFLINE-------------- \n "+e1);
				
				JOptionPane.showMessageDialog(LaminaCliente.this, "Servidor Offline");
			}
		}
		

		
	}
	
	private class RecibeTexto implements Runnable{


		public void run() {

			try {
				
			
				String nick,ip,mensaje;
				boolean online;
				
				PaqueteEnvio paquete_recibido=new PaqueteEnvio();
				
				ServerSocket server_recepcion = new ServerSocket(9090);
			
				while(true){
					
					Socket socket_recepcion=server_recepcion.accept();
				
					ObjectInputStream objeto_recibido = new ObjectInputStream (socket_recepcion.getInputStream());
				
					paquete_recibido= (PaqueteEnvio) objeto_recibido.readObject();
					
					nick=paquete_recibido.getNick();
					ip=paquete_recibido.getIp();
					mensaje=paquete_recibido.getMensaje();
					online=paquete_recibido.getOnline();
					
			
					InetAddress ipHost= InetAddress.getLocalHost();
				
					campoChat.append(nick + ": " + mensaje + "\n");
					campoChat.append("ip local: " + ipHost.getHostAddress() + "\n");
					campoChat.append("ip recepcion: " + socket_recepcion.getInetAddress() + "\n");
					
				
					//server_recepcion.close();
				
				}
							
			} catch (IOException e) {

				System.out.println("-----------Problemas creando ServerSocket-------------- \n" + e);

			}catch (ClassNotFoundException e){
				
				System.out.println("-----------Problemas recibiendo Objeto-------------- \n");
			}
			
		}
		
	}
	
	
	private String nick_entrada;
	private JTextField textoEnvio, nick, ip;
	private JButton botonEnvio;
	private JTextArea campoChat;

	
}



class PaqueteEnvio implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String nick, ip, mensaje;
	private boolean online;
	
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

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public boolean getOnline() {
		return online;
	}


}
