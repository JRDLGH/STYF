package chat.view;

import java.util.Observer;

import chat.controller.ServerController;
import chat.model.Server;
/**
 * 
 * @author JRDN
 *
 */
public abstract class ServerView implements Observer{
	protected Server server;
	protected ServerController controller;
	/**
	 * This constructor get the server model and the controller in order to
	 * connect them together.
	 * @param server
	 * @param controller
	 */
	public ServerView(Server server, ServerController controller){
		this.server = server;
		this.controller = controller;
		server.addObserver(this); // On établis la connexion vue-modèle
	}
	public abstract void display(String s);
}
