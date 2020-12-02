package com.wordchaingame.server;

import java.net.InetAddress;

public class ServerClient {
	public String name;
	public InetAddress address;
	public int port;
	public final int ID;
	public int attempt = 0;
	public int score = 0;
	public boolean turn = false;
	
	public ServerClient (String name, InetAddress address, int port, final int ID) {
		this.ID = 0;
		this.name = name;
		this.address = address;
		this.port  = port; 
		
	}
	
	public int getID() {
		return ID;
	}
	
}
