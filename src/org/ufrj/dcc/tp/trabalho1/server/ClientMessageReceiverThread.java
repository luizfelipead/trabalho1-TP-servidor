package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import com.google.gson.Gson;

//Thread que gerencia a conexao com um socket. Vai enviar/receber as mensagens de/para um cliente.
public class ClientMessageReceiverThread extends Thread {
	
	private static final String GOODBYE_MSG = "EXIT";
	private static final Gson GSON = new Gson();
	private static final long TIME_TO_DISCONNECT_CLIENT = 15000;
	
	//Armazeno o servidor criador da thread para manipular a sua lista de clientes conectados, 
	//alem de enviar uma mensagens a todos os clientes conectados
	private Server server;
	private Socket socket;
	private int clientId;
	
	private Scanner in;
	private PrintStream out;
	
	private Timer timerToDisconnectClient = new Timer();
	
	private class DisconnectTimerTask extends TimerTask{

		private ClientMessageReceiverThread parentThread;
		
		public DisconnectTimerTask(ClientMessageReceiverThread parent){
			this.parentThread = parent;
		}
		
		@Override
		public void run() {
			try {
				System.out.println("[INFO] TIMEOUT <ID:"+parentThread.getClientId()+">, Disconnecting client");
				parentThread.sendDisconnectMessage();
				parentThread.closeSocket();				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public ClientMessageReceiverThread(int id, Server server, Socket socket) throws IOException{
		this.server = server;
		this.socket = socket;
		this.setClientId(id);
		in = new Scanner(socket.getInputStream());
		out = new PrintStream(socket.getOutputStream(),true);

		//Disconecto o cliente caso eu nao receba nenhum PING por 15 segundos
		timerToDisconnectClient.schedule(
				new DisconnectTimerTask(this), TIME_TO_DISCONNECT_CLIENT, TIME_TO_DISCONNECT_CLIENT);
	}
	
	public void sendDisconnectMessage() {
		ChatMessage chatMessage = new ChatMessage(0, "<ID:"+clientId+"> foi desconectaco!", ChatMessage.PUBLIC_MESSAGE);
		server.sendToConnectedClients(chatMessage);
		
		server.getConnectedClients().remove(getClientId());
		
		List<Integer> ids = new ArrayList<Integer>();
		for (ClientMessageReceiverThread client : server.getConnectedClients().values()) {
			ids.add(client.getClientId());
		}
		server.sendToConnectedClients(new ListClientsMessage(ids));	
	}

	public PrintStream getOut() {
		return out;
	}

	public void setOut(PrintStream out) {
		this.out = out;
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
				try{
					while(in.hasNextLine()){
						String message = in.nextLine();
						System.out.println("[INFO] Message received from <ID:"+this.getClientId()+"> "+message);
						try {
							if (message.equals(GOODBYE_MSG)){
								this.closeSocket();
								sendDisconnectMessage();
								//kills thread
								return;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
						ChatMessage chatMessage = GSON.fromJson(message, ChatMessage.class);
						if (chatMessage.getType() == ChatMessage.PING){
							System.out.println("[DEBUG] Ping received from <ID:"+clientId+">");
							timerToDisconnectClient.cancel();
							timerToDisconnectClient = new Timer();
							timerToDisconnectClient.schedule(new DisconnectTimerTask(this), TIME_TO_DISCONNECT_CLIENT, TIME_TO_DISCONNECT_CLIENT);
							
						} else {
							chatMessage.setFromClientId(clientId);
							
							if (chatMessage.getType() == ChatMessage.PUBLIC_MESSAGE){
								server.sendToConnectedClients(chatMessage);
							} else {
								server.sendToConnectedClient(chatMessage);
							}
						}
					}
				} catch (Exception e){
					return;
				}
			}
	}

	protected void closeSocket() throws IOException {
		System.out.println("[INFO] Closing connection for client ID "+ getClientId());
		in.close();
		out.close();
		server.getConnectedClients().remove(this);
		socket.close();
		timerToDisconnectClient.cancel();
	}

}
