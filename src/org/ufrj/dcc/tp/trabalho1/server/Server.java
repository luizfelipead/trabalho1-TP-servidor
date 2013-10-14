package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class Server {
	
	private int port;
	private ServerSocket serverSocket;
	private Map<Integer, ClientMessageReceiverThread> connectedClients = new HashMap<Integer, ClientMessageReceiverThread>();
	private static final Gson GSON = new Gson();
	
	public Server(int port){
		this.port=port;
	}
	
	public void init() throws IOException{
		System.out.println("[INFO] Starting server...");
		try {
			serverSocket = new ServerSocket(this.port);
			System.out.println("[INFO] Server successfully started at "+serverSocket.getInetAddress().getHostAddress());
			int idCounter = 1;
			List<Integer> ids = new ArrayList<Integer>();
			while (true){
				Socket clientSocket = serverSocket.accept();
				System.out.println("[INFO] Connection estabilished with a client. ID: "+idCounter+". Connection from: "+clientSocket.getInetAddress());
				
				ChatMessage joinedMessage = new ChatMessage(0, "<ID:"+idCounter+"> acabou de se conectar!", ChatMessage.PUBLIC_MESSAGE);
				sendToConnectedClients(joinedMessage);
				
								
				ClientMessageReceiverThread connectionManagerThread = new ClientMessageReceiverThread(idCounter, this, clientSocket);
				connectedClients.put(idCounter, connectionManagerThread);
				connectionManagerThread.start();
				
				sendIdToClient(idCounter++);
				
				ids.clear();
				for (ClientMessageReceiverThread client : connectedClients.values()) {
					ids.add(client.getClientId());
				}
				
				sendToConnectedClients(new ListClientsMessage(ids));				
			}
		} catch (IOException e) {
			System.out.println("[ERROR] Connection error. Port already in use?");
		}
	}

	private void sendIdToClient(int id) {
		Message message = new Message();
		message.setType(Message.SET_ID);
		message.setToId(id);
		
		getConnectedClients().get(id).getOut().println(GSON.toJson(message));
	}

	public void sendToConnectedClients(Message message) {
		for (ClientMessageReceiverThread client : connectedClients.values()) {
			if (!(message.getType() == Message.PUBLIC_MESSAGE && message.getFromId() == client.getClientId())) {
				client.getOut().println(GSON.toJson(message));
			}	
		}
	}

	public Map<Integer, ClientMessageReceiverThread> getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(Map<Integer, ClientMessageReceiverThread> connectedClients) {
		this.connectedClients = connectedClients;
	}

	public void sendToConnectedClient(ChatMessage chatMessage) {
		getConnectedClients().get(chatMessage.getToId()).getOut().println(GSON.toJson(chatMessage));
	}
}
