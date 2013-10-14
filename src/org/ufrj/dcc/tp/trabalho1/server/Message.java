package org.ufrj.dcc.tp.trabalho1.server;

public class Message {
	private int type;
	private int toId;
	private int fromId;
	
	public static final int SET_ID = -3;
	public static final int LIST_CLIENT = -2;
	public static final int PING = -1;
	public static final int PUBLIC_MESSAGE = 0;
	public static final int PRIVATE_MESSAGE = 1;
	
	public static final int SERVER_ID = 0;
	public static final int PING_ID = 0;
	
	
	public int getToId() {
		return toId;
	}
	public void setToId(int type) {
		this.toId = type;
	}
	public int getFromId() {
		return fromId;
	}
	public void setFromId(int fromId) {
		this.fromId = fromId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
