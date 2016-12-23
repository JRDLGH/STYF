package chat.view;
/**
 * @author Cedric lambin
 */
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import chat.controller.ServerController;
import chat.model.Server;

public class ServerGUI extends ServerView{
	 
	private JFrame ServeurJFrame;
	private javax.swing.JTextField tf_port = new JTextField("1500");
	private javax.swing.JTextArea textArea = new JTextArea();
	private javax.swing.JButton b_start = new JButton("Start") ;
	private javax.swing.JButton b_end = new JButton("End");
	private javax.swing.JLabel lb_port = new JLabel("Port : ");
	private javax.swing.JPanel contentPane = new JPanel();
	private javax.swing.JTextField tf_commande = new JTextField();
	private javax.swing.JButton b_commande = new JButton("Send");
	

	public ServerGUI(Server server, ServerController controller) {
		super(server, controller);
		this.ServeurJFrame = new JFrame("Serveur");
		
		
		ServeurJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ServeurJFrame.setBounds(100, 100, 552, 394);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		ServeurJFrame.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lb_port.setBounds(26, 26, 56, 16);
		contentPane.add(lb_port);
		
		
		tf_port.setEditable(true);
		tf_port.setBounds(98, 23, 116, 22);
		contentPane.add(tf_port);
		tf_port.setColumns(10);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(26, 70, 453, 180);
		contentPane.add(scrollPane);
		
		scrollPane.setViewportView(textArea);
		textArea.setEditable(false);
		
		JLabel label = new JLabel("");
		label.setBounds(26, 26, 56, 16);
		contentPane.add(label);
		
		
		b_start.setBounds(24, 300, 97, 25);
		b_start.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_startActionPerformed(evt);
			}
		});
		contentPane.add(b_start);
		
		b_end.setBounds(379, 300, 97, 25);
		contentPane.add(b_end);
		b_end.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_endActionPerformed(evt);
			}
		});
		
		tf_commande.setBounds(26, 263, 341, 22);
		contentPane.add(tf_commande);
		tf_commande.setColumns(10);
		
	
		
		b_commande.setBounds(379, 262, 97, 25);
		contentPane.add(b_commande);
		b_commande.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent evt){
				b_commandeActionPerformed(evt);
			}
		});
		
		ServeurJFrame.setVisible(true);
	}
	private void b_commandeActionPerformed(java.awt.event.ActionEvent evt){
		
		controller.listCommande(tf_commande.getText());
	}
	
	
	private void b_startActionPerformed(java.awt.event.ActionEvent evt){
		
		controller.isPortValid(Integer.parseInt(tf_port.getText()));
		System.out.println(Integer.parseInt(tf_port.getText()));
		
		
		
	}
	private void b_endActionPerformed(java.awt.event.ActionEvent evt){
		
		controller.stopServer();
		
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
