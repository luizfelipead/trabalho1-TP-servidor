package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;

//Thread que gerencia a conexao com um socket. Vai enviar/receber as mensagens de/para um cliente.
public class ClientMessageReceiverThread extends Thread {
	
	private static final String GOODBYE_MSG = "EXIT";
	private static final Gson GSON = new Gson();
	
	//Armazeno o servidor criador da thread para manipular a sua lista de clientes conectados, 
	//alem de enviar uma mensagens a todos os clientes conectados
	private Server server;
	private Socket socket;
	private int clientId;

	private Scanner in;
	
	public ClientMessageReceiverThread(int id, Server server, Socket socket) throws IOException{
		this.server = server;
		this.socket = socket;
		this.setClientId(id);
		in = new Scanner(socket.getInputStream());
		
	}
	
	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run(){
			while (true){
				while(in.hasNextLine()){
					String message = in.nextLine();
					System.out.println("[INFO] Message received from <ID:"+this.getClientId()+"> "+message);
					try {
						if (message.equals(GOODBYE_MSG)){
							this.closeSocket();
							this.interrupt();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					ChatMessage chatMessage = new ChatMessage(clientId, message);
					server.sendToConnectedClients(chatMessage);
				}
			}
		
	}

	private void closeSocket() throws IOException {
		System.out.println("[INFO] Closing connection for client ID "+ getClientId());
		in.close();
		server.getConnectedClients().remove(this);
		socket.close();
	}

}
