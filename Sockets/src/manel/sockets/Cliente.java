package manel.sockets;

import java.awt.*;
import java.awt.event.*;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.*;

public class Cliente {

	public static void main(String[] args) {
		
		
		MarcoCliente marcoCliente=new MarcoCliente();
		
		marcoCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


	}

}


class MarcoCliente extends JFrame{
	
	public MarcoCliente(){
		
		this.setBounds(300, 300, 400, 550);
		
		this.setTitle("Cliente");
		
		LaminaCliente laminaCliente=new LaminaCliente();
		
		this.add(laminaCliente);
		
		this.setVisible(true);
		
	}
	
	
}


class LaminaCliente extends JPanel{
	
	
	public LaminaCliente(){
		
		
		this.setLayout(new BorderLayout());
		
		JLabel etiqueta=new JLabel("CLIENTE", JLabel.CENTER);
		
		textoEnvio =new JTextField (20);
		
		JButton botonEnvio=new JButton("Enviar");
		
		botonEnvio.addActionListener(new EnviaTexto());
		
		this.add(etiqueta, BorderLayout.NORTH);
		
		this.add(textoEnvio,BorderLayout.CENTER);
		
		this.add(botonEnvio, BorderLayout.SOUTH);
		
		
	}
	
	private class EnviaTexto implements ActionListener{


		public void actionPerformed(ActionEvent e) {


			try {
				
				miSocket=new Socket("192.168.1.99",9999);
				DataOutputStream flujo_salida = new DataOutputStream(miSocket.getOutputStream());
				flujo_salida.writeUTF(textoEnvio.getText());
				miSocket.close();
				
			} catch (IOException e1) {

				System.out.println("-----------Problemas creando Socket-------------- \n "+e1);
			}
		}
		
	Socket miSocket;	
		
	}
	
	JTextField textoEnvio;
	
}
