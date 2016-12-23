/**
 * 
 */
package client.view;

import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import client.controller.Controller;
import client.model.Client;

/**
 * This console interface works in the follow step:
 * Welcome the user, query the user if he wants to sign in or sign up,
 * In function on user choice (sign in or sign up),  the console get information
 * and verify there credibility in database.
 * Once all login information are collected, the client has to chose the conversation he
 * wants to enter and if this conversation is valid, the client finally has to chose the
 * port to use for communication.
 *  Once all of this is done, if a server listens to the given port, the client will try to
 *  enable connection with the server. 
 * @author JRDN
 *
 */
public class Console extends View implements Observer{
	private Scanner sc; //used to scan user information
	private Scanner i;
	
	/**
	 * This constructor call the super abstract class View in order to
	 * link Client client and Controller controller to the view but also
	 * create a new Scanner, that will be used to collect user information.
	 * Finally, this constructor start thread "ReadInput".  
	 * @param client, the model to link to this view
	 * @param controller, the controller to link to this view
	 */
	public Console(Client client, Controller controller){
		super(client,controller);
		sc = new Scanner(System.in);
		new Thread(new ReadInput()).start();
	}
	@Override
	/**
	 * This method display the current conversation of the user.
	 * The utility of this method is kind of useless for the moment
	 * but can be used in further improvement.
	 */
	public void update(Observable o, Object arg) {
		display(client.getUsername() + " your current conversation is " + client.getConversation());
	}
	/**
	 * This conversation simply verify if the string passed is not null or empty
	 * and if not, display the string passed.
	 * @param s, the string to display
	 */
	public void display(String s) {
		if(s != null && s != ""){
			System.out.println(s);
		}else{
			System.out.println(s);
		}
	}
	/**
	 * This method log the user if his username is valid.
	 * A valid username is an alphabetical name present in the database.
	 * @param username, the username of the client
	 */
	public void logMe(String username){
		if(controller.isUsernameValid(username,1)){
			String password= "";
			display("Enter your password: ");
			password = sc.nextLine();
			controller.log(username, password);
		}
	}
	/**
	 * This method display a welcome message for the new user.
	 */
	public void welcomeMsg(){
		display("***************************");
		display("Hi you and welcome on STYF!");
		display("Are you already registred(Y/N)?");
	}
	/*
	 * This method generate a registration form.
	 * The user will be invited to create his account with
	 * username and password.
	 * Errors are displayed if username or password verifications
	 * failed.
	 */
	public void registerForm(){
		while(true){
			display("Enter your username:");
			String username = sc.nextLine();
			if(controller.isUsernameValid(username,0)){
				display("Enter your password:");
				String pass = sc.nextLine();
				String err = controller.createUser(username, pass);
				if(err == null){
					break;
				}else{
					display(err);
				}
			}
		}
	}
	/**
	 * This thread initialize the client.
	 * For more detail, this thread ask the client to enter informations like:
	 * If the client is already a member of STYF Community, if not, the thread
	 * demand to the client to SIGN UP.
	 * If the client is already a member, the thread ask client username, password
	 * and the conversation to join.
	 * Finally, when all theses steps are done without errors, the server ask the client
	 * to enter the port for communication.
	 * Once done, the client is connected to the server and ready to speak with his friends!
	 * @author JRDN
	 *
	 */
	private class ReadInput implements Runnable{
		public void run(){
			while(true){
				welcomeMsg();
				String answ = sc.nextLine().toUpperCase();
				if(controller.getAnswer(answ) == 'Y'){
					break;
				}else if(controller.getAnswer(answ) == 'N'){
					registerForm();
				}
			}
			while(true){
				String username = "";
				display("Enter your username(only alphabetical character are allowed):");
				username = sc.nextLine();
				logMe(username);
				if(client.isLogged()){
					break;
				}
			}
			while(true){
				String conversation = "";
				display("Which conversation do you want to join? (Enter D for Default)");
				conversation = sc.nextLine();
				if(conversation.equals("d") || conversation.equals("D")){
					conversation = "default";
				}
				if(controller.isConversationValid(conversation))break;
			}
			i = new Scanner(System.in);
			while(true){
				int port = 0;
				display("Server IP:");
				String ip = i.nextLine();
				try{
					display("Enter a port number between [1024-65535]: ");
					port = sc.nextInt();
					if(controller.isPortValid(port,ip)){
						break;
					}
				}catch(InputMismatchException e){
					display(">> Invalid value, please enter a valid port number. Valid port"
							+ " are between 0 and 65535 but in this case"
							+ " please choose between 1024 and 65535");
					sc.next();
				}catch(Exception ex){
					display(">> Invalid value, please enter a number !");
					sc.next();
				}
			}
		}
	}
}
