package com.wordchaingame.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;



public class ClientWindow extends JFrame implements Runnable{
	private JPanel contentPane;
	private JTextField inputTxtField;
	private JTextArea scoreboaredTxtArea; 
	private JLabel instrutionLabel;
	private JLabel feedbackLabel;
	private JLabel timerLabel;
	private Thread listen, run;
	private boolean running = false;

	
	private Client client;
	private int score;
	private int timer;
	private char currentLetter;
		
	public ClientWindow(String playerName, String address, int port) {
		// connection
		client = new Client(playerName, address, port);
		System.out.println("Attempting a connection to "+ address + ":"+ port + "usesr:"+playerName);

		boolean connected = client.connect(address,port);
		if(!connected) {
			System.out.println("CONNECTION FAILED!!!");
		} else {
			// creates window
			createWindow();
			System.out.println("SUCCESFUL connection to "+ address + ":"+ port + " user:  "+playerName);
			String connection = "/c/" + playerName;
			client.send(connection.getBytes());
			running = true;
			run = new Thread(this, "Running");
			run.start();
		}
		
		
		
		
		
	}
	
	public void createWindow() {
		setTitle("Word Chain Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1280, 720);
		setLocationRelativeTo(null);
	
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground( new Color(137, 74, 243));
		
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{ 75, 1280/8, 1280/8, 1280/8,1280/8, 1280/8, 1280/8,  1280/8, 1280/8};
		gbl_contentPane.rowHeights = new int[]{720/8, 720/8,720/8, 720/8,720/8, 720/8,720/8, 720/8 };
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		timerLabel = new JLabel("Timer: "+this.getTimer());
		timerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		GridBagConstraints gbc_timerLabel = new GridBagConstraints();
		gbc_timerLabel.insets = new Insets(0, 0, 5, 5);
		gbc_timerLabel.gridx = 4;
		gbc_timerLabel.gridy = 0;
		contentPane.add(timerLabel, gbc_timerLabel);
		
		JLabel playerLabel = new JLabel(client.getName());
		playerLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		GridBagConstraints gbc_playerLabel = new GridBagConstraints();
		gbc_playerLabel.insets = new Insets(0, 0, 5, 5);
		gbc_playerLabel.gridx = 7;
		gbc_playerLabel.gridy = 0;
		contentPane.add(playerLabel, gbc_playerLabel);
		
		 scoreboaredTxtArea = new JTextArea();
		scoreboaredTxtArea.setEditable(false);
		GridBagConstraints gbc_scoreboaredTxtArea = new GridBagConstraints();
		gbc_scoreboaredTxtArea.gridwidth = 2;
		gbc_scoreboaredTxtArea.gridheight = 6;
		gbc_scoreboaredTxtArea.insets = new Insets(0, 0, 5, 5);
		gbc_scoreboaredTxtArea.fill = GridBagConstraints.BOTH;
		gbc_scoreboaredTxtArea.gridx = 1;
		gbc_scoreboaredTxtArea.gridy = 1;
		contentPane.add(scoreboaredTxtArea, gbc_scoreboaredTxtArea);
		
		instrutionLabel = new JLabel("Word That Starts With "+getCurrentLetter());
		instrutionLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
		instrutionLabel.setForeground(Color.WHITE);
		GridBagConstraints gbc_instrutionLabel = new GridBagConstraints();
		gbc_instrutionLabel.gridwidth = 4;
		gbc_instrutionLabel.insets = new Insets(0, 0, 5, 5);
		gbc_instrutionLabel.gridx = 4;
		gbc_instrutionLabel.gridy = 1;
		contentPane.add(instrutionLabel, gbc_instrutionLabel);
		
		feedbackLabel = new JLabel("");
		feedbackLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		feedbackLabel.setForeground(new Color(0, 0, 0));
		GridBagConstraints gbc_feedbackLabel = new GridBagConstraints();
		gbc_feedbackLabel.gridwidth = 4;
		gbc_feedbackLabel.insets = new Insets(0, 0, 5, 5);
		gbc_feedbackLabel.gridx = 4;
		gbc_feedbackLabel.gridy = 2;
		contentPane.add(feedbackLabel, gbc_feedbackLabel);
		
		JButton sendBtn = new JButton("Submit");
		sendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				// if not match 
				send();
			}

		});
		
		inputTxtField = new JTextField();
		inputTxtField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==10) {
					send();
				}
			}
		});
		inputTxtField.setHorizontalAlignment(SwingConstants.CENTER);
		inputTxtField.setFont(new Font("Calibri", Font.PLAIN, 20));
		GridBagConstraints gbc_inputTxtField = new GridBagConstraints();
		gbc_inputTxtField.gridwidth = 4;
		gbc_inputTxtField.fill = GridBagConstraints.BOTH;
		gbc_inputTxtField.insets = new Insets(0, 0, 5, 5);
		gbc_inputTxtField.gridx = 4;
		gbc_inputTxtField.gridy = 4;
		contentPane.add(inputTxtField, gbc_inputTxtField);
		inputTxtField.setColumns(10);
		GridBagConstraints gbc_sendBtn = new GridBagConstraints();
		gbc_sendBtn.anchor = GridBagConstraints.EAST;
		gbc_sendBtn.gridwidth = 2;
		gbc_sendBtn.insets = new Insets(0, 0, 5, 5);
		gbc_sendBtn.gridx = 6;
		gbc_sendBtn.gridy = 5;
		contentPane.add(sendBtn, gbc_sendBtn);
		
		
		
		
		setVisible(true);
		// update scoreboard
		updateScoreBoard();
	}
	


	//*********************************************
	public void updateScoreBoard() {
		scoreboaredTxtArea.setText("");
		scoreboaredTxtArea.append(client.getName() + ":"+ this.score+"\n\r");
		// for all other players??? get from client?? get from server??
	}
	
	//*********************************************************** NEEDS TO GO TO SERVER
	public void send() {
		
    	String input = inputTxtField.getText().toUpperCase();
		// if match
		if(input.length() > 0 && input.charAt(0)== getCurrentLetter()) {
				
			feedbackLabel.setText("GREAT!!!!");
			feedbackLabel.setForeground(Color.green);
			inputTxtField.setText("");
			
			char newLetter = input.charAt(input.length()-1);
			String entry = "/m/" + newLetter + ":" + getTimer();
			client.send(entry.getBytes());
			
			
		} else {
		
			feedbackLabel.setText("Sorry, Try Again!!!!");
			feedbackLabel.setForeground(Color.red);
		
			
		}
		
		// after a few seconds remove feedback
		// TO-DO
	}

	
	//-------------------------------------
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while(running) {
					String message = client.receive();
					if(message.startsWith("/c/")){
							// System.out.println(message.length());
							// /c/12345/e/  
							String stringID = message.split("/c/|/e/")[1];
							System.out.println(stringID.length());
							client.setID(Integer.parseInt(stringID));
							System.out.println("Succefully connected to the server! ID: "+ client.getID());
					} else if (message.startsWith("/m/")) {
						// update everything
						updateScoreBoard();
					} else if (message.startsWith("/init/")) {
						// set all names, letters, scores, and etc
						String[] msgArr = message.split("/init/|/e/|:");
						setTimer(Integer.parseInt(msgArr[2]));
						setCurrentLetter(msgArr[1].charAt(0));
						System.out.println(Arrays.toString(msgArr));
					} else if(message.startsWith("/update/")){
						String[] msgArr = message.split("/update/|/e/|:");
						setTimer(Integer.parseInt(msgArr[2]));
						setCurrentLetter(msgArr[1].charAt(0));
		
					} else {
						System.out.println("i got this message:   "+message);
					}
				}
			}

		
		};
		listen.start();
	
	}


	public void run() {
		listen();
	}

	private void setCurrentLetter(char charAt) {
		// TODO Auto-generated method stub
		this.currentLetter = charAt;
		instrutionLabel.setText("Word That Starts With "+getCurrentLetter()); 
	}

	private char getCurrentLetter() {
		// TODO Auto-generated method stub
		return currentLetter;
	}
	
	private int getTimer() {
		// TODO Auto-generated method stub
		return timer;
	}
	private void setTimer(int time) {
		this.timer = time;
		timerLabel.setText(getTimer()+"");
	}
}
