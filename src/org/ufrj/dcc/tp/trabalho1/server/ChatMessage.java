package org.ufrj.dcc.tp.trabalho1.server;

public class ChatMessage {
	
	public static final int PING = 0;
	public static final int PUBLIC_MESSAGE = 1;
	
	private int type;
	private int fromClientId;
	private String message;
	
	public ChatMessage(String message, int type) {
		this.message=message;
		this.type=type;
	}
	
	public ChatMessage(int clientId, String message, int type) {
		this.message=message;
		this.type=type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

