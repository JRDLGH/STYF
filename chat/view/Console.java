package chat.view;

import java.util.InputMismatchException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import chat.controller.Controller;
import chat.model.Model;

/**
 * This console view will display information for the users
 * and, will allow interaction between user and server.
 * @author JRDN
 *
 */
//CETTE CLASSE DOIT ÊTRE MISE A JOUR SI CHANGEMENT DU MODELE
// S'accroche au modèle via model.addObserver(this)
//il faudra implémenter update() -> sera appellé lors de changements
//CHAQUE VUE EST LIÉE A UN CONTRÔLEUR, c'est le contrôleur qui
//s'occupera du traitement des changements demandés par l'user via la vue
//Quand traitement fini, on envoi le résultat à la vue.
public class Console extends View implements Observer{
	/**
	 * The input for the scanner.
	 */
	protected Scanner sc;
	/**
	 * Link the model and the controller, initialize the scanner and
	 * create the thread in charge of reading the scanner input.
	 * @param model
	 * @param controller
	 */
	public Console(Model model, Controller controller){
		super(model,controller);
		sc = new Scanner(System.in);
		new Thread (new ReadInput()).start();
	}
	@Override
	/**
	 * Update the view with new and fresh data.
	 */
	public void update(Observable o, Object arg) {
		display("Connection on port " + model.getPort() + "..."); // On chope le port
	}
	/**
	 * The method join display the connection state when a 
	 * user join the chat room with the port specified.
	 * @port communication entry
	 */
	public void join(int port) {
		display("Establishing the connection with the server...");
		controller.startServer(port);
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
			sc.close();
		}
	}

	@Override
	/**
	 * Close the connection and display the connection states.
	 */
	public void leave() {
		// TODO Auto-generated method stub
		System.out.println("Leave?");
	}
	/**
	 * Display the string specified.
	 * @param s, the string to display
	 */
	public void display(String s){
		System.out.println(s);
	}
}
