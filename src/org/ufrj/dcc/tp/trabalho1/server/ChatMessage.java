package org.ufrj.dcc.tp.trabalho1.server;

public class ChatMessage extends Message {
		
	private int fromClientId;
	private String message;
	
	public ChatMessage(String message, int type) {
		this.message=message;
		this.setType(type);
	}
	
	public ChatMessage(int clientId, String message, int type) {
		this.message=message;
		this.setType(type);
	}

	public int getFromClientId() {
		return fromClientId;
	}

	public void setFromClientId(int fromClientId) {
		this.fromClientId = fromClientId;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

