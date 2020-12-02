package com.wordchaingame.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;


public class Server implements Runnable{
	private int port;
	private DatagramSocket socket;
	private Thread run, manage, send, receive;
	private boolean running = false; 

	private List<ServerClient> clients = new ArrayList<ServerClient>();
	
	// game details
	private char currentLetter = 'G';
	private int timer = 37;
	private TreeMap<String, String> dictionary;
	private String turn = null;
	
		
	// TIMER THREAD on your turn 
	public Server(int port) {
 		this.port = port;
 		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}
 		// NOTE: this refers to this Server instances run() method. allows it to run.
 		run = new Thread(this, "Server");
 		run.start(); // otherwise it will auto terminate
 	}
	// runs once to start the server 
	public void run() {
		
		running = true;
		System.out.println("Server started on port "+port);
		System.out.println("Current Letter:"+ getCurrentLetter());
		manageClients();
		receive();
	}
 	
	// sending pings to check if other clients are still there
 	private void manageClients() {
 		manage = new Thread("Manage") {
 			public void run() {
 				while(running) {
 					// Managing
 					// TO-DO
 				}
 			}
 		};
 		manage.start();
 	}
 	
 	// receive all data from clients
 	private void receive() {
 		receive = new Thread("Receive") {
 			public void run() {
 				while(running) {
 					// Receiving
 					byte[] data = new byte[1024];
 					DatagramPacket packet = new DatagramPacket(data, data.length);
 					try {
						socket.receive(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
 					process(packet);
 					
 					
 						
 				}
 			}
 		};
 		receive.start();
 	}
 	
 	// update
 	private void sendToAll(String entry) {
 		for(int i = 0; i < clients.size(); i++) {
 			ServerClient client = clients.get(i);
 			send(entry, client.address, client.port);
 		}
 		
 	}
 	
 	private void send(final byte[] data, final InetAddress address, final int port) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		send.start();
	}
 	
 	private void send(String message, final InetAddress address, int port) {
 		message += "/e/";
 		send(message.getBytes(), address, port);
 	}
 	
 	private void process(DatagramPacket packet) {
 		String string = new String(packet.getData());
			
 		if(string.startsWith("/c/")){
 			// UUID id = UUID.randomUUID();
 			int id = UniqueIdentifier.getIdentifier();
 			clients.add(new ServerClient(string.substring(3, string.length()), packet.getAddress(), packet.getPort(), id));
 			System.out.println(string.substring(3, string.length()));
 			System.out.println("IDENTIFIER: "+id);
 			String ID = "/c/" + id;
 			// sends ID
 			send(ID, packet.getAddress(), packet.getPort());
 			
 			
 			// update client
 			String init = "/init/" + this.getCurrentLetter() + ":" + this.getTimer();
 			send(init, packet.getAddress(),packet.getPort());
 			
 		} else if (string.startsWith("/m/")){
 			// needs to be an update
 			System.out.println(string);
 			String strArr[] = string.substring(3,string.length()).split(":");
 			// update this players score with strArr[1];
 			System.out.println(strArr[0]);
 			setCurrentLetter(strArr[0].charAt(0));
 			
 			// update all clients
 			// update client
 			String update = "/update/" + this.getCurrentLetter() + ":" + this.getTimer();		
 			sendToAll(update);
 		} else {
 		
 			System.out.println(string);
 		}
 	}
 	
 	
	public boolean isInDictionary(String word) {
		return dictionary.containsKey(word);
	}

	public void setDictionary(TreeMap<String, String> dictionary) {
		this.dictionary = dictionary;
	}

	public char getCurrentLetter() {
		return currentLetter;
	}

	public void setCurrentLetter(char currentLetter) {
		this.currentLetter = currentLetter;
	}
	public int getTimer() {
		return this.timer;
	}
	public int setTimer(int time) {
		return	this.timer = time;
	}

 	
 	
}
