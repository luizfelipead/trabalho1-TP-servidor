package org.ufrj.dcc.tp.trabalho1.server;

import java.io.IOException;

public class ServerMain {

	public static void main(String[] args) throws IOException {
		Server s = new Server(2004);
		s.init();

	}

}
