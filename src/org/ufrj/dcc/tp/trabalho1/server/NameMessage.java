package org.ufrj.dcc.tp.trabalho1.server;



public class NameMessage extends ChatMessage {

	public NameMessage(String name, int fromId) {
		super(name, fromId);
		setType(NAME_MESSAGE);
	}
	
}