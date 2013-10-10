package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
	
	private int port;
	private ServerSocket serverSocket;
	private List<Socket> connectedClients = new ArrayList<Socket>();

	public Server(int port){
		this.port=port;
	}
	
	public void init() throws IOException{
		System.out.println("Starting server...");
		try {
			serverSocket = new ServerSocket(this.port);
			System.out.println("Server successfully started at "+serverSocket.getLocalSocketAddress());
			while (true){
				Socket clientSocket = serverSocket.accept();
				System.out.println("Connection estabilished from: "+clientSocket.getInetAddress());
				connectedClients.add(clientSocket);
				ConnectionManagerThread connectionManagerThread = new ConnectionManagerThread(this, clientSocket);
				connectionManagerThread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			serverSocket.close();
		}
	}

	public void sendToConnectedClients(String message) {
		for (Socket clientSocket : connectedClients) {
			try {
				PrintStream out = new PrintStream(clientSocket.getOutputStream());
				out.println(message);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<Socket> getConnectedClients() {
		return connectedClients;
	}

	public void setConnectedClients(List<Socket> connectedClients) {
		this.connectedClients = connectedClients;
	}
	
	

}
