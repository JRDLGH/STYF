package client.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import client.controller.Controller;
import client.model.Client;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

public  class Client_GUI extends View {
//thread pour une interface graphique, s'inspirer de la console, récuperer les info apres avoir appuyé sur le bouton send
	private JFrame ClientJFrame;
	private JPanel contentPane;
	private javax.swing.JTextField tf_port = new JTextField();
	private javax.swing.JTextField tf_users = new JTextField();
	private javax.swing.JTextField tf_Ip = new JTextField();
	private javax.swing.JTextField tf_send = new JTextField();
	private javax.swing.JTextField tf_convers = new JTextField("Default");
	private javax.swing.JTextArea textArea = new JTextArea();
	private javax.swing.JButton b_send = new JButton("send");
	private javax.swing.JButton b_connection = new JButton("connection");
	private javax.swing.JButton b_disconnect = new JButton("disconnection");
	private javax.swing.JButton b_Inscription = new JButton("Inscription");
	
	private int idCom = 0;
	
	public Client_GUI(Client client, Controller controller) {
		super(client, controller);
		this.ClientJFrame = new JFrame("Client");
		
		ClientJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ClientJFrame.setBounds(100, 100, 627, 429);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		ClientJFrame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblPort = new JLabel("Port :");
		lblPort.setBounds(60, 13, 56, 16);
		contentPane.add(lblPort);
		
		JLabel lblConvers = new JLabel("Conversation :");
		lblConvers.setBounds(306, 41, 91, 16);
		contentPane.add(lblConvers);
		
		tf_convers.setBounds(408,39,116,22);
		contentPane.add(tf_convers);
		tf_convers.setColumns(10);
		
		
		
		tf_port.setBounds(101, 10, 116, 22);
		contentPane.add(tf_port);
		tf_port.setColumns(10);
		tf_port.setEditable(true);
		
		
		JLabel lblUtilisateurs = new JLabel("Users :");
		lblUtilisateurs.setBounds(50, 42, 78, 16);
		contentPane.add(lblUtilisateurs);
		
		
		tf_users.setBounds(99, 39, 116, 22);
		contentPane.add(tf_users);
		tf_users.setColumns(10);
		tf_users.setEditable(true);
		
		JLabel lblAdresseIp = new JLabel("Ip address :");
		lblAdresseIp.setBounds(321, 13, 75, 16);
		contentPane.add(lblAdresseIp);
		
		tf_Ip = new JTextField();
		tf_Ip.setBounds(408, 10, 116, 22);
		contentPane.add(tf_Ip);
		tf_Ip.setEditable(true);
		tf_Ip.setColumns(10);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 70, 490, 210);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);
		
		
		b_connection.setBounds(26, 344, 97, 25);
		contentPane.add(b_connection);
		b_connection.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_connectActionPerformed(evt);
			}
		});
		
		
		b_disconnect.setBounds(456, 344, 116, 25);
		contentPane.add(b_disconnect);
		b_disconnect.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_disconnectActionPerformed(evt);
			}
		});
		
		
		tf_send.setBounds(26, 293, 375, 37);
		contentPane.add(tf_send);
		tf_send.setColumns(10);
		
		
		b_send.setBounds(419, 293, 97, 37);
		contentPane.add(b_send);
		b_send.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_sendActionPerformed(evt);
			}
		});
		
		b_Inscription.setBounds(135, 343, 97, 25);
		contentPane.add(b_Inscription);
		b_Inscription.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_InscriptionActionPerformed(evt);
			}
		});
		ClientJFrame.setVisible(true);
	}
	
	
	
	private void b_InscriptionActionPerformed(java.awt.event.ActionEvent evt){
		try {
			Inscription frame = new Inscription(client, controller);
			frame.setVisible(true);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void b_connectActionPerformed(java.awt.event.ActionEvent evt){
		
		if(controller.isUsernameValid(tf_users.getText(),1)){
			if(controller.isConversationValid("default")){
				controller.isPortValid(Integer.parseInt(tf_port.getText()),tf_Ip.getText());
			}
			
		};
			
		
	}
	private void b_disconnectActionPerformed(java.awt.event.ActionEvent evt){
		controller.stop();
	}
	

	private void b_sendActionPerformed(java.awt.event.ActionEvent evt){
		if(idCom == 0){
			controller.setServerConnected(true);
			controller.identify();
		}
		idCom++;
		controller.sendGUI(tf_send.getText());
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void display(String s) {
		textArea.append("\n" + s);
		
	}

}
