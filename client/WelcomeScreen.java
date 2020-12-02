package com.wordchaingame.client;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WelcomeScreen extends JFrame {

	private JPanel contentPane;
	private JTextField nameInput;
	private String playerName;
	private String ipAddress = "localhost";
	private int port = 2300;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WelcomeScreen frame = new WelcomeScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public WelcomeScreen() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Word Chain Game - WELCOME");
		setSize(1280, 720);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getContentPane().setBackground( new Color(137, 74, 243));
		
		
		JLabel welcomeLabel = new JLabel("Word Chain");
		welcomeLabel.setBackground(Color.WHITE);
		welcomeLabel.setFont(new Font("Calibri", Font.BOLD, 32));
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setBounds(515, 146, 250, 102);
		contentPane.add(welcomeLabel);
		
		JButton playBtn = new JButton("Join Game");
		playBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			
				init(nameInput.getText());
			}
		});
		playBtn.setBounds(590, 388, 100, 39);
		// playBtn.setBackground( new Color(225, 225, 225) );
		 playBtn.setBorder(null);
		 playBtn.setFocusPainted(false);
		
		contentPane.add(playBtn);
		
		JLabel nameLabel = new JLabel("What is your name?");
		nameLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		nameLabel.setBounds(568, 255, 160, 33);
		contentPane.add(nameLabel);
		
		nameInput = new JTextField();
		nameInput.setText("Player1");
		nameInput.setForeground(Color.WHITE);
		nameInput.setBackground(Color.BLACK);
		nameInput.setFont(new Font("Comic Sans MS", Font.PLAIN, 11));
		nameInput.setBounds(568, 290, 147, 25);
		contentPane.add(nameInput);
		nameInput.setColumns(10);
		

	}

	protected void init(String name) {
		System.out.println("initializing game");
		// check if server open??? 
		dispose();
		new ClientWindow(name, "localhost", 2300);
	}
}
