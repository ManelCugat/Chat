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
	
	/**
	 * 
	 */
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
	
	
	/**
	 * 
	 */
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
			
				while (true){
				
				Socket misocket=socketServidor.accept();
			
				DataInputStream textoEntrada=new DataInputStream (misocket.getInputStream());
			
				String texto=textoEntrada.readUTF();
				
				areaTexto.append(texto + "\n");
				
				misocket.close();
				
				}
			
			} catch (IOException e) {

				System.out.println("-----------Problemas creando Socket Servidor --------- \n "+e);
		
			}
		}
	
	JTextArea areaTexto;
	
}



