package com.wordchaingame.server;

// creates the instance of the server
public class ServerMain {
	private  int port;
	private  Server server;
	
	public ServerMain(int port) {
		this.port = port;
		server = new Server(port);
		
	}
	
	public static void main(String [] args) {
		int port;
    	if(args.length != 1) {
    		System.out.println("Usage: java WindowScreen [port]");
    		return;
    
    	}
    	
    	if(args.length == 0) {
    		port = 2300;
        }
	
		port = Integer.parseInt(args[0]);
		new ServerMain(port);
		
	
	}
	
}
