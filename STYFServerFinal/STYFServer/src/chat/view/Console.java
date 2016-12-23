package chat.view;

import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import chat.controller.ServerController;
import chat.model.Server;

/**
 * Console view for server.
 * @author JRDN
 *
 */
public class Console extends ServerView implements Observer{
	protected Scanner sc;
	protected ServerController controller;
	public Console(Server server, ServerController controller){
		super(server,controller);
		this.controller = controller;
		sc = new Scanner(System.in);
		new Thread (new ReadInput()).start();
	}
	/**
	 * ReadInput read the port newPort number entered by the user.
	 * @author JRDN
	 * @exception une InputMismatchException est lancée
	 * si newPort n'est pas un integer
	 * @exception une Exception est lancée dans les autres cas 
	 */
	private class ReadInput implements Runnable{ // Permet de faire des actions en //
		public void run(){
			/**
			 * Boolean variable set to false in order to keep the loop working.
			 */
			boolean quit = false;
			/**
			 * The port number.
			 */
			int newPort;
			/**
			 * In this loop, the integer newPort is set by a user and get by the scanner.
			 * quit define if the loop continue or not.
			 */
			while(quit == false){
				System.out.println("Port: ");
				try{
					newPort = 0;
					newPort = sc.nextInt();
					//if(newPort < 0 && newPort > 655335)
					quit = controller.isPortValid(newPort);
					// controller part
				}catch(InputMismatchException e){
					display(">> Invalid value, please enter a valid port number.");
					sc.next();
				}catch(Exception ex){
					display(">> Invalid value, please enter a number between 0 and 65535!");
					sc.next();
				}
			}
			command();
		}
		/**
		 * This method check if a command is entered and detect what to do.
		 */
		public void command(){
			boolean leave = false;
			while(leave == false){
				String m = "";
				m = sc.nextLine();
				switch(m){
					case "stop":controller.stopServer();
						leave = true;
						break;
					case "show u":controller.showConnectedUsers();;
						break;
					case "show c":controller.showConversationsNames();;
						break;
					case "help": showCommand();
						break;
					default:display("#? Command not found. Type help.");
				}
			}
			sc.close();
		}
	}
	/**
	 * Show all the command, this method is called if user type "help"
	 */
	public void showCommand(){
		display("#>You will find all the command you can use in the following list");
		display("show u/c\n\tu users, show connected users\n\tc conversations, show active conversations name "
				+ ", the number of client connected in the conversation and show inactive conversations.");
		display("stop\n\t shutdown the server and all communications.");
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		display(server.getClientName(server.getClients().size()-1) + " is now connected.");
	}
	/**
	 * Display a simple message.
	 */
	@Override
	public void display(String s) {
		System.out.println(s);
		
	}
}
