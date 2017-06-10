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
		
		nick_entrada=JOptionPane.showInputDialog("Por favor, Introduce tu nickname");
		
		nick=new JTextField(10 );
	
		JLabel etiqueta=new JLabel("--CHAT--", JLabel.CENTER);
		
		ip = new JTextField(10);
	
		textoEnvio =new JTextField (30);
	
		campoChat=new JTextArea (20,30);
		
		botonEnvio=new JButton("Enviar");
		
		botonEnvio.addActionListener(new EnviaTexto());
		
		nick.setText(nick_entrada);
		
		nick.setEnabled(false);
		
		this.add(nick);
		
		this.add(etiqueta);
		
		this.add(ip);
		
		this.add(campoChat);
		
		this.add(textoEnvio);
		
		this.add(botonEnvio);
		
		Thread t = new Thread(new RecibeTexto());
		
		t.start();

	
	}
	
	private class EnviaTexto implements ActionListener{


		public void actionPerformed(ActionEvent e) {


				
			
			try {
				
				miSocket=new Socket("192.168.1.99",9999);
				
				PaqueteEnvio datos=new PaqueteEnvio();
						
				datos.setNick(nick_entrada);
				
				datos.setIp(ip.getText());
				
				datos.setMensaje(textoEnvio.getText());
				
				campoChat.append(nick_entrada + ": " +textoEnvio.getText() + "\n");
				
				textoEnvio.setText("");
				
				ObjectOutputStream paquete_envio = new ObjectOutputStream (miSocket.getOutputStream());
				
				paquete_envio.writeObject(datos);
				
				paquete_envio.close();
				
				paquete_envio.close();

				miSocket.close();
				
			} catch (IOException e1) {

				System.out.println("-----------Servidor OFFLINE-------------- \n "+e1);
				
				JOptionPane.showMessageDialog(LaminaCliente.this, "Servidor Offline");
			}
		}
		
	Socket miSocket;	
		
	}
	
	private class RecibeTexto implements Runnable{


		public void run() {

			try {
				
			
				String nick,ip,mensaje;
				
				PaqueteEnvio paquete_recibido=new PaqueteEnvio();
				
				ServerSocket server_recepcion = new ServerSocket(9090);
			
				while(true){
				
					Socket socket_recepcion=server_recepcion.accept();
				
					ObjectInputStream objeto_recibido = new ObjectInputStream (socket_recepcion.getInputStream());
				
					paquete_recibido= (PaqueteEnvio) objeto_recibido.readObject();
					
					nick=paquete_recibido.getNick();
					ip=paquete_recibido.getIp();
					mensaje=paquete_recibido.getMensaje();
				
					campoChat.append(nick + ": " + mensaje + "\n");
				
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
