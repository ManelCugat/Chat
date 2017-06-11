package manel.sockets;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;

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
	JTextArea areaTexto;
	ArrayList <UsuarioOnline> usuariosOnline = new ArrayList <UsuarioOnline>();
	

	public LaminaServidor(){
		
		
		this.setLayout(new BorderLayout());
		
		areaTexto =new JTextArea ();
		
		this.add(areaTexto, BorderLayout.CENTER);
		
		Thread t=new Thread(this);
		
		t.start();
		
	}
	

	public void run() {
	

				ServerSocket socketServidor = null;
				
				try {
					socketServidor = new ServerSocket(9999);
				
				} catch (IOException e) {
	
					System.out.println("-----------Problemas en el Servidor ecuchando de Cliente-------------- \n" + e);
				}
				
				String nick=null;
				String ip=null;
				String mensaje=null;
				PaqueteEnvio paquete_recibido=null;
				InetAddress ipReceptor;
			
				while (true){
				
					try{
					
					Socket misocket = socketServidor.accept();
			
					ObjectInputStream objeto_recibido = new ObjectInputStream(misocket.getInputStream());
			
					paquete_recibido=(PaqueteEnvio) objeto_recibido.readObject();
				
					nick=paquete_recibido.getNick();
				
					ip=paquete_recibido.getIp();
				
					mensaje=paquete_recibido.getMensaje();
					
					ipReceptor=misocket.getInetAddress();
					
					System.out.println("ip del receptor: " + ipReceptor);
					
					UsuarioOnline uo=new UsuarioOnline(nick,ipReceptor);
					
					System.out.println("Creado usuario: " + uo.getNickName());
					
					if (!compruebaOnline(uo,ipReceptor)){
						
						areaTexto.append(nick + " con ip: " + ipReceptor + " ONLINE\n");
						usuariosOnline.add(uo);
			
					}else areaTexto.append(nick +": " + mensaje + " para: " + ip + "\n");
				
					
					/*System.out.println("________ONLINE USERS_______");
					
					for (UsuarioOnline onlineUser: usuariosOnline){
						
						System.out.println(onlineUser.toString());
						
					}*/
					
					misocket.close();
					
					}catch (IOException e){
						
					System.out.println("-----------Error socket servidor escuhando cliente-------------- \n "+e);
						
			
					}catch (ClassNotFoundException e){
					
					System.out.println("-----------Error datos recibidos en servidor-------------- \n "+e);	
						
					}
			
					//El servidor reenvia el mensaje al cliente destinatario
					
					try{
					
					Socket envia_destinatario=new Socket (ip,9090);
					
					ObjectOutputStream paquete_reenvio=new ObjectOutputStream(envia_destinatario.getOutputStream());
					
					paquete_reenvio.writeObject(paquete_recibido);
					
					paquete_reenvio.close();
					
					envia_destinatario.close();
				
					}catch (IOException e){
						
						System.out.println("-----------Error Servidor en reenvio de mensaje a cliente-------------- \n "+e);	
						
						
						
					}
			

				}
			
	}
	
	public boolean compruebaOnline(UsuarioOnline user, InetAddress ip){
		
		System.out.println("Entra el método compruebaOnline");
		
		boolean isOnline=false;
		
		if (usuariosOnline.isEmpty()){
			
			System.out.println("Array vacío");
			
			usuariosOnline.add(user);
			
			areaTexto.append(user.getNickName() + " con ip: " + user.getIp() + " ONLINE\n");
			
			isOnline=true;
	
		}
		
		Iterator <UsuarioOnline> it = usuariosOnline.iterator();
		
		while (it.hasNext()){
			
			UsuarioOnline uo=it.next();
			
			if (uo.getIp().equals(ip)){
				
				isOnline= true;
				
				System.out.println("La ip ya existe");
		
			}
		
		}
		
		return isOnline;
		
		
	}
	

	private class UsuarioOnline{
		
		
		private String nickName;
		private InetAddress ip;
		
		public UsuarioOnline(String nickName, InetAddress ip){
			
			this.nickName=nickName;
			this.ip=ip;
			
		}
		
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public InetAddress getIp() {
			return ip;
		}

		public void setIp(InetAddress ip) {
			this.ip = ip;
		}
		
		public String toString(){
			
			return nickName + " con ip: " +ip + " is Online!!";
		}
		
		
	}
	

	
}







