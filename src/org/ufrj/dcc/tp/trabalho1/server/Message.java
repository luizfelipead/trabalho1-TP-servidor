package org.ufrj.dcc.tp.trabalho1.server;

public abstract class Message {
	private int type;
	
	public static final int LIST_CLIENT = -2;
	public static final int PING = -1;
	public static final int PUBLIC_MESSAGE = 0;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
