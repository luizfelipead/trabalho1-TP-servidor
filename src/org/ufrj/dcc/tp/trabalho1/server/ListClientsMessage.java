package org.ufrj.dcc.tp.trabalho1.server;

import java.util.List;

public class ListClientsMessage  extends Message {
	private List<Integer> clientsIds;

	public ListClientsMessage(List<Integer> clientsIds) {
		this.setType(LIST_CLIENT);
		this.clientsIds = clientsIds;
	}
	
	public List<Integer> getClientsIds() {
		return clientsIds;
	}

	public void setClientsIds(List<Integer> clientsIds) {
		this.clientsIds = clientsIds;
	}
}
