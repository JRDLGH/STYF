package client.view;

import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.controller.Controller;
import client.model.Client;

public class Inscription extends View {

	private JPanel contentPane;
	private JTextField tf_users= new JTextField();
	private JTextField tf_pass = new JTextField();
	private JFrame JFInscription = new JFrame(); 
	private JTextArea textArea = new JTextArea();
	

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public Inscription(Client client, Controller controller) {
		super(client, controller);
		this.JFInscription = new JFrame();
		JFInscription.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFInscription.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		JFInscription.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel_Users = new JLabel("Users  :");
		lblNewLabel_Users.setBounds(25, 28, 56, 16);
		contentPane.add(lblNewLabel_Users);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(53, 117, 243, 55);
		contentPane.add(scrollPane);
		
		
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);
		
		JTextField tf_users= new JTextField();
		tf_users.setBounds(93, 25, 116, 22);
		contentPane.add(tf_users);
		tf_users.setColumns(10);
		
		JLabel lblNewLabel_Pass = new JLabel("password :");
		lblNewLabel_Pass.setBounds(12, 57, 69, 16);
		contentPane.add(lblNewLabel_Pass);
		
		JTextField tf_pass = new JTextField();
		tf_pass.setBounds(93, 54, 116, 22);
		contentPane.add(tf_pass);
		tf_pass.setColumns(10);
		
		JButton b_Sign = new JButton("Sign up");
		b_Sign.setBounds(22, 191, 97, 25);
		contentPane.add(b_Sign);
		b_Sign.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_SignActionPerformed(evt);
			}
		});
		
		JButton b_back = new JButton("Back");
		b_back.setBounds(298, 191, 97, 25);
		contentPane.add(b_back);
		b_back.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_backActionPerformed(evt);
			}
		});
		
	}
	
	private void b_SignActionPerformed(java.awt.event.ActionEvent evt){
		if(controller.isUsernameValid(tf_users.getText(), 0)){
			controller.createUser(tf_users.getText(), tf_pass.getText());
		
		}
		
	}
	private void b_backActionPerformed(java.awt.event.ActionEvent evt){
		JFInscription.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//System.exit(0);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void display(String s) {
		textArea.append("\n" + s);
		
	}

	public void setVisible(boolean b) {
		// TODO Auto-generated method stub
		
	}

}
