package manel.sockets;

import java.awt.*;
import java.io.*;
import java.net.*;

import javax.swing.*;


public class Servidor {

	public static void main(String[] args) {

		
		MarcoServidor marcoServidor=new MarcoServidor();
		
		marcoServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	}

}


class MarcoServidor extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public MarcoServidor(){
		
		
		this.setBounds(900, 300, 400, 550);
		
		this.setTitle("Servidor");
		
		laminaCliente=new LaminaServidor();
		
		this.add(laminaCliente);
		
		this.setVisible(true);
		
	}


public JPanel getLamina(){
	
	return laminaCliente;
	
	
}

LaminaServidor laminaCliente;
	
	
}


class LaminaServidor extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;

	public LaminaServidor(){
		
		
		this.setLayout(new BorderLayout());
		
		areaTexto =new JTextArea ();
		
		this.add(areaTexto, BorderLayout.CENTER);
		
		Thread t=new Thread(this);
		
		t.start();
		
	}
	
	public void run() {
		

			try {
				
				@SuppressWarnings("resource")
				ServerSocket socketServidor=new ServerSocket(9999);
				
				String nick,ip,mensaje;
				
				PaqueteEnvio paquete_recibido;
			
				while (true){
				
					Socket misocket=socketServidor.accept();
				
					ObjectInputStream objeto_recibido=new ObjectInputStream(misocket.getInputStream());
			
					paquete_recibido=(PaqueteEnvio) objeto_recibido.readObject();
				
					nick=paquete_recibido.getNick();
				
					ip=paquete_recibido.getIp();
				
					mensaje=paquete_recibido.getMensaje();
				
					areaTexto.append(nick +": " + mensaje + " para: " + ip + "\n");
				
					misocket.close();
				
					//El servidor reenvia el mensaje al cliente destinatario
					
					Socket envia_destinatario=new Socket (ip,9090);
					
					ObjectOutputStream paquete_reenvio=new ObjectOutputStream(envia_destinatario.getOutputStream());
					
					paquete_reenvio.writeObject(paquete_recibido);
					
					envia_destinatario.close();
				
				}
			
			} catch (IOException e) {

				System.out.println("-----------Problemas creando Socket Servidor --------- \n "+e);
		
			} catch (ClassNotFoundException e){
				
				System.out.println("-----------Problemas recibiendo paquete en servidor --------- \n "+e);
				
			}
		}
	
	JTextArea areaTexto;
	
}



