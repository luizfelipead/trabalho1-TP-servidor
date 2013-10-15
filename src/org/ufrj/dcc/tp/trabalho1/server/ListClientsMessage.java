package org.ufrj.dcc.tp.trabalho1.server;

import java.util.Map;

public class ListClientsMessage  extends Message {
	private Map<Integer, String> clientsIds;

	public ListClientsMessage(Map<Integer, String> clientsIds) {
		this.setType(LIST_CLIENT);
		this.setClientsIds(clientsIds);
	}

	public Map<Integer, String> getClientsIds() {
		return clientsIds;
	}

	public void setClientsIds(Map<Integer, String> clientsIds) {
		this.clientsIds = clientsIds;
	}
	
}
