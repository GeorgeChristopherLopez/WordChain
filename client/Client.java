package com.wordchaingame.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Client  {
	private static final long serialVersionUID = 1L;
	
	private DatagramSocket socket;

	private String playerName;
	private String address;
	private int port;
	private InetAddress ip;
	private Thread send;
	private int ID;
		
	public Client(String playerName, String address, int port) {
		this.playerName = playerName;
		this.address = address;
		this.port = port;
	}
	
	public String getName() {
		return this.playerName;
	}
	
	public String getAddresss() {
		return this.address;
	}
	
	public int getPort() {
		return this.port;
	}
		

	public boolean connect(String address, int port) {
		try {
			// this will throw error with SERVER AT 2300 **************************
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}catch (SocketException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
		
	}
	
	
	
	// receiving messages ? score? names? current letter?
	public String receive() {
		byte[] data = new byte [1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		
		try {
			socket.receive(packet);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		return message;
	}
	
	public void send(final byte[]data) {
		send = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		send.start();
	}

	public void setID(int id) {
	   this.ID= id;
		
	}
	
	public int getID() {
		return this.ID;
	}
	
	// listen???
	// updates ui 
	
	
}
