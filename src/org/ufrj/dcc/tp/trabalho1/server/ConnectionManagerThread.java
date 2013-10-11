package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

//Thread que gerencia a conexao com um socket. Vai enviar/receber as mensagens de/para um cliente.
public class ConnectionManagerThread extends Thread {
	
	private static final String GOODBYE_MSG = "EXIT";
	
	//Armazeno o servidor criador da thread para manipular a sua lista de clientes conectados, 
	//alem de enviar uma mensagens a todos os clientes conectados
	private Server server;
	private Socket socket;
	
	private Scanner in;
	
	public ConnectionManagerThread(Server server, Socket socket) throws IOException{
		this.server = server;
		this.socket = socket;
		in = new Scanner(socket.getInputStream());
		
	}
	
	@Override
	public void run(){
		try{
			
			while (true){
				while(in.hasNextLine()){
					String message = in.nextLine();
					System.out.println(message);
					if (message.equals(GOODBYE_MSG)){
						this.closeSocket();
						//TODO remover da lista
						break;
					}
					//server.sendToConnectedClients(message);
				}
			}
		} catch (Exception e){
			e.printStackTrace();
			server.getConnectedClients().remove(this);
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			in.close();
		}
	}

	private void closeSocket() throws IOException {
		System.out.println("Closing connection from "+socket.getLocalAddress());
		in.close();
		socket.close();
		server.getConnectedClients().remove(this);
	}
}
