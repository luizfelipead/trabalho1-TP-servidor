package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class Server {
	
	private int port;
	private ServerSocket serverSocket;
	private List<ClientMessageReceiverThread> connectedClients = new ArrayList<ClientMessageReceiverThread>();
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
			while (true){
				Socket clientSocket = serverSocket.accept();
				System.out.println("[INFO] Connection estabilished with a client. ID: "+idCounter+". Connection from: "+clientSocket.getInetAddress());
				ChatMessage joinedMessage = new ChatMessage(0, "<ID:"+idCounter+"> acabou de se conectar!");
				sendToConnectedClients(joinedMessage);
				ClientMessageReceiverThread connectionManagerThread = new ClientMessageReceiverThread(idCounter++, this, clientSocket);
				connectedClients.add(connectionManagerThread);
				connectionManagerThread.start();
			}
		} catch (IOException e) {
			System.out.println("[ERROR] Connection error. Port already in use?");
		}
	}

	public void sendToConnectedClients(ChatMessage message) {
		for (ClientMessageReceiverThread client : connectedClients) {
			client.getOut().println(GSON.toJson(message));
		}
	}

	public List<ClientMessageReceiverThread> getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(List<ClientMessageReceiverThread> connectedClients) {
		this.connectedClients = connectedClients;
	}
	
	

}
