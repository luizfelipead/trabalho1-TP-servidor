package org.ufrj.dcc.tp.trabalho1.server;

public class ChatMessage {
	
	private int clientId;
	private String message;
	
	public ChatMessage(int clientId, String message) {
		this.clientId=clientId;
		this.message=message;
	}
	public int getClientId() {
		return clientId;
	}
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
