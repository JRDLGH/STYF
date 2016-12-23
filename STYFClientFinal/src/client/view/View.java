package client.view;

import java.util.Observer;

import client.controller.Controller;
import client.model.Client;

/**
 * The mother class view handle the display for users
 * Initialize multiple abstract methods for daughter classes.
 * @author JRDN
 *
 */
public abstract class View implements Observer{
	protected Client client;
	protected Controller controller;
	/**
	 * View construtor initialize the connection between the model
	 * and the view itself.
	 * @param model
	 * @param controller
	 */
	public View(Client client, Controller controller){
		this.client = client;
		this.controller = controller;
		client.addObserver(this); // Connexion vue-modele
	}
	public abstract void display(String s);
	/*@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}*/
	
}
