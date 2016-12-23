/**
 * 
 */
package client.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import client.model.Client;
import client.view.View;

/*
 * Une fois que l'on s'est connecté sur le serveur, envoyer l'adresse ip du client, son userId, son username
 * pour permettre au serveur d'identifier le client puis le serveur, une fois qu'il a tout valider
 * envois le numéro de la conversation auquel le client appartient et il redistribuera les messages aux autres
 * membres faisant partie de la même conversation.
 * 
 * RAJOUTER L'HEURE A L'ENVOI DU MSG
*/


/**
 * @author JRDN
 *
 */
public class Controller{
	private Client client = null;
	private View view = null;
	private Socket clientSocket = null;
	//private Scanner getMsg;
	private Scanner sendMsg = new Scanner(System.in);
	private boolean identified = false;
	private ObjectOutputStream objOut;
	private boolean serverConnected = false;
	/*
	 * This constructor allow the controller to get the model called Client
	 */
	public Controller(Client cl){
		client = cl;
	}
	/**
	 * Define if the port and the ip is valid or not.
	 * If port and ip are both valid, the client can start.
	 * @param port, the port gave by the user
	 * @param ip, the ip of the server
	 * @return 	false if the port is egal to 0, is not between 1024 and 65535 or
	 * if the server is down,
	 * it finally returns true if the port is valid and server is active. 
	 */
	public boolean isPortValid(int port,String ip){
		int error = 0;
		if(port == 0){
			view.display(">> Please enter a not null port number!");
			error++;
		}else if(port < 1024 || port > 65535){
			view.display(">> Wrong port number. Please enter a port between 1024 and 65535");
			error++;
		}else if(ip.equals("") || ip.length()<=8){
			view.display("Please enter a correct ip!");
			error++;
		}
		else if(error == 0){
			start(port,ip);
			if(error == 0 && client.getIsClientConnected()){
				client.setPort(port);
				return true;
			}
		}
		return false;
	}
	/**
	 * This method verify if the string passed in argument respect
	 * the followed restrictions:
	 * The string can't:
	 * be null, be equals to nothing or a space, 
	 * have a length inferior of 4,
	 * and can be composed of any special character.
	 * @param check, the string to verify
	 * @return true if the string respects restrictions
	 * 		   false if the string is doesn't respect restriction
	 */
	public boolean isValid(String check){
		int error = 0;
		boolean valid = false;
		if(check == null || check == "" || check == " "){
			view.display("!Please enter something at least!");
			error++;
		}else if(check.length() < 4){
			view.display("!Your username is too short!");
			error++;
		}else if(!check.matches("^[a-zA-Z]*$")){
			view.display("!Number and special caracter are not authorized!");
			error++;
		}
		if(error == 0){
			valid=true;
		}
		return valid;
	}
	/**
	 * This method creates an customized user in the database.
	 * @param username, the username of the user, it needs to respect restrictions
	 * @param password, the password of the user, composed of at least 4 chars.
	 * @return an error, in order to prevent the caller of any errors.
	 */
	public String createUser(String username, String password){
		String err = null;
		if(isValid(username)){
			int error=0;
			if(password == null){
				err="Please enter a password";
				error++;
			}else if(password.length()<4){
				err="Your password is too short, please enter more than 4 character.";
				error++;
			}
			if(error == 0){
				err = client.subscribe(username, password);
				if(err == null){
					view.display("Welcome in STYF community " + username + "!");
					view.display("Do not forget your password!");
				}	
			}
		}else{
			err="Username invalid!";
		}
		return err;
	}
	/**
	 * Returns the first char of the string in parameters.
	 * @param answ, the string
	 * @return the first char of the string
	 */
	public char getAnswer(String answ){
		char finalAnsw;
		finalAnsw = answ.charAt(0); // On prend le 1er caractère
		return finalAnsw;
	}
	/**
	 * This method define if the username gave by the user is valid or if it's not.
	 * @param username, gave by the user
	 * @param fct, is used to define the function of the code:
	 * 0: for registration verification
	 * 1: for log verification
	 * @return false, if the username contains special caracters or numbers
	 * true, if the username is alphabetical
	 */
	public boolean isUsernameValid(String username, int fct){
		int error = 0;
		boolean valid=false;
		if(isValid(username)){
			if(!client.isUniqueUsername(username)){
				if(fct==1){
					view.display("This username doesn't exist.");
					error++;
				}else if(fct==0){
					valid=true;
					view.display("This username is free.");
				}
			}else if(error == 0){
				if(fct==1){
					client.setUsername(username);
					view.display("Welcome " + client.getUsername() + "!");
					valid=true;
				}else if(fct==0 && client.isUniqueUsername(username)){
					view.display("Username already taken by another.");
				}
			}
		}
		return valid;
	}
	/**
	 * This method define if the conversation gave by the user is valid or if it's not.
	 * @param converation, gave by the user
	 * @return false, if the conversation name contains special caracters or numbers
	 * true, if the conversation name is alphabetical
	 */
	public boolean isConversationValid(String name){
		int error = 0;
		if(name == null || name == "" || name == " "){
			view.display("!Please enter something at least!");
			error++;
		}else if(name.length() < 4){
			view.display("!Conversation name is too short, enter at least 4 chars!");
			error++;
		}else if(name.equals(client.getUsername())){
			view.display("!You can't use your own name as a conversation name!");
			error++;
		}else if(error == 0){
			client.setConversation(name);
			return true;
		}
		return false;
	}
	public void sendGUI(String msg){
		PrintWriter out = null;
		//setServerConnected(true);
		//identify();
		String msgS;
		if(isIdentified()){
			try {
				out = new PrintWriter(clientSocket.getOutputStream());
				//sendMsg = new Scanner(System.in);
					//view.display("Your message: ");
					msgS = msg;
					if(!msgS.equals("") && !msgS.equals("stopServer")){
						//On empèche le client d'envoyer "" car sert à rien
						//On empèche le client d'envoyer stopServer car ça stope le socket client
						out.println(msgS);
						view.display("You :" + msg);
					}else{
						view.display("You are not authorized to send that kind of message!");
					}
					if(command(msgS) || msgS == null){
					
					}
					out.flush(); // On vide la sortie
				
			} catch (IOException e) {
				view.display("!Error sending message!");
			}
		}else{
			view.display("SERVER > Client is not indentified");
		}
		
	}
	/**
	 * This thread invite the client to enter a message and if the message is correct
	 * to send it to server. 
	 * @author JRDN
	 */
	private class Send implements Runnable{
		private PrintWriter out = null;
		public void run(){
			setServerConnected(true);
			identify();
			String msgS;
			if(isIdentified()){
				try {
					out = new PrintWriter(clientSocket.getOutputStream());
					while(isServerConnected()){
						view.display("Your message: ");
						msgS = sendMsg.nextLine();
						if(isCorrectMsg(msgS)){
							out.println(msgS);
						}
						if(command(msgS) || msgS == null){
							break;
						}
						out.flush(); // On vide la sortie
					}
					sendMsg.close();
				} catch (IOException e) {
					view.display("!Error sending message!");
				}
			}else{
				view.display("SERVER > Client is not indentified");
			}
		}
	}
	/**
	 * This method verify if the message gave as argument fit the restrictions and returns
	 * a boolean if the message is correct or not.
	 * Restrictions are: a message not bigger than 80 chars and not empty.
	 * @param msg, the message to verify
	 * @return true if the message is correct
	 * 		   false if the message is incorrect
	 */
	public boolean isCorrectMsg(String msg){
		boolean correct = false;
		if(msg.length()>80){
			view.display("Please, do not enter a too long message! Max: 80 character");
		}
		else if(msg.equals("")){
			view.display("Pleaser enter a message!");
		}else{
			correct = true;
		}
		return correct;
	}
	/**
	 * This thread is used to receive message from server.
	 * @author JRDN
	 *
	 */
	private class Receive implements Runnable{
		private String msgR;
		private BufferedReader in = null;
		public void run(){
			try{
				if(clientSocket.isConnected() && !clientSocket.isClosed()){
					in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				}
				while(!clientSocket.isClosed()){//socket open
					msgR = "";
					msgR = in.readLine();
					if(msgR == null){
						setServerConnected(false);
						stop();
						break;
					}
					view.display(msgR);
				}
			}catch(IOException e){
				if(clientSocket.isClosed()){
					view.display("You are now disconnected!");
				}else{
					view.display("Server disconnected.");
				}			
			}
		}
	}
	/**
	 * This method is very important, it sends a object message to server.
	 * This object message contains the username, the conversation, the identify state,
	 * the connection state of the client.
	 * Once received, the client is accepted or not by the server.
	 * If identify method runs without errors, 
	 * the client can continue, or stop if there is errors.
	 * 
	 */
	public void identify(){
		boolean success = false;
			try{
				objOut = new ObjectOutputStream(clientSocket.getOutputStream());
		        objOut.flush();
		        String[] identifiers = {client.getUsername(),client.getConversation(),
		        		"false","connected"};
		        objOut.writeObject(identifiers);
		        objOut.flush();
		        success = true;
			}catch(IOException io){
				io.printStackTrace();
				view.display(">> Error in identification.");
			}catch(NullPointerException c){
				c.printStackTrace();
				view.display(">> Stream is null.");
			}
		setIdentified(success);
	}
	/**
	 * This method verify if the username is unique and log the user.
	 * @param username, the username to verify and the client to log.
	 * @param password, the password of the client.
	 */
	public void log(String username, String password){
		if(client.isUniqueUsername(username)){
			String err = client.logAndCheck(username, password);
			if(client.logAndCheck(username, password) == null){
				view.display("You are successfully logged.");
				client.setLogged(true);
			}else{
				view.display(err);
			}
		}
	}
	/**
	 * This method start the server with port and the server ip
	 * @param port, the port listenned by server
	 * @param ip, the server ip
	 */
	public void start(int port,String ip){
		boolean isStarted = false;
		if(view != null){ // if view is not null then
			//connect
			try{
				if(client.getIsClientConnected()){
					view.display(">> The client is already connected to the server.");
				}else{
					clientSocket = new Socket(ip,port); //IP = mettre celle du serveur
					if(clientSocket.isConnected() && clientSocket.isClosed() == false){
						view.display("You are now connected to the server.");
						view.display("Type 'stop' in order to end the communication");
						isStarted = true;
						new Thread(new Send()).start();
						new Thread(new Receive()).start();
					}
				}
			}catch(UnknownHostException e){
				view.display(">> Unknown Host!");
				isStarted=false;
			}catch(IOException i){
				view.display(">> Error during starting: The server is down.");
			}
		}else{
			System.out.println(">> $CONTROLLER - Fatal error: No view or invalid port.");
		}
		client.setIsClientConnected(isStarted);
	}
	/**
	 * This method stop the client and display a message if client is correctly closed or not.
	 */
	public void stop(){
		boolean isStarted = true;
		if(clientSocket != null){
			if(client.getIsClientConnected()){
				try{
					clientSocket.close();
					if(clientSocket.isClosed()){
						if(!isServerConnected()){
							view.display("Connection closed by the server.");
							sendMsg.close();
						}
						isStarted = false;
					}
				}
				catch(Exception io){
					view.display(">> Client failed to disconnect.");
				}
			}
		}
		client.setIsClientConnected(isStarted);
	}
	/**This method is used to execute a specific command and to returns
	 * boolean, the success of the method.
	 * @param cmd, the string that represents the command and used inside the switch to
	 * get the specified method.
	 * @return true if method success, false if failure.
	 */
	public boolean command(String cmd){
		boolean done = false;
		if(cmd != null){
			switch(cmd){
			case "stop": stop();
				if(client.getIsClientConnected() == false){done = true;}
				else{done=false;}
				break;
			default:;
			}
		}
		return done;
	}
	/**
	 * Set the connection between the controller and the view.
	 * @param view, the view connected to the controller.
	 */
	/**Returns the client socket of the user
	 * @return the clientSocket
	 */
	public Socket getClientSocket() {
		return clientSocket;
	}
	/**Set the clientSocket used for all communications.
	 * @param clientSocket the clientSocket to set
	 */
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	/**Returns if client is identified
	 * @return the identify state
	 */
	public boolean isIdentified() {
		return identified;
	}
	/**Define if the client is identified and ready.
	 * This method is used to confirm the success of method identify()
	 * @param identified, will be set to true if identify succeeds,
	 *                    will be set to false if identify fails
	 */
	public void setIdentified(boolean identified) {
		this.identified = identified;
	}
	/**
	 * Get the server state.
	 * @return true if the server is connected,
	 * 		   false if the server is disconnected
	 */
	public boolean isServerConnected() {
		return serverConnected;
	}
	/**
	 * Set the server state.
	 * @param serverConnected
	 */
	public void setServerConnected(boolean serverConnected) {
		this.serverConnected = serverConnected;
	}
	/**
	 * This method link the controller a view.
	 * @param cons, the view console.
	 */
	public void addView(View cons) {
		this.view=cons;
	}
}
