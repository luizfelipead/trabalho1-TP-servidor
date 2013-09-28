package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

//Thread que gerencia a conexao com um socket. Vai enviar/receber as mensagens de/para um cliente.
public class ConnectionManagerThread extends Thread {
	
	private static final String GOODBYE_MSG = "EXIT";
	
	//Armazeno o servidor criador da thread para manipular a sua lista de clientes conectados, 
	//alem de enviar uma mensagens a todos os clientes conectados
	private Server server;
	private Socket socket;
	
	private ObjectInputStream in;
	
	public ConnectionManagerThread(Server server, Socket socket) throws IOException{
		this.server = server;
		this.socket = socket;
		
		try{
			while (true){
				in = new ObjectInputStream(socket.getInputStream());
				String message = (String)in.readObject();
				if (message.equals(GOODBYE_MSG)){
					this.closeSocket();
					break;
				}
				server.sendToConnectedClients(message);
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			in.close();
			socket.close();
			server.getConnectedClients().remove(this);
		}
		
	}

	private void closeSocket() throws IOException {
		System.out.println("Closing connection from "+socket.getLocalAddress());
		in.close();
		socket.close();
		server.getConnectedClients().remove(this);
	}
}
