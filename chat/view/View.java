package chat.view;

import java.util.Observer;

import chat.controller.Controller;
import chat.model.Model;

/**
 * The mother class view handle the display for users
 * Initialize multiple abstract methods for daughter classes.
 * @author JRDN
 *
 */
public abstract class View implements Observer{
	protected Model model;
	protected Controller controller;
	/**
	 * View construtor initialize the connection between the model
	 * and the view itself.
	 * @param model
	 * @param controller
	 */
	public View(Model model, Controller controller){
		this.model = model;
		this.controller = controller;
		model.addObserver(this); // Connexion vue-modele
	}
	public abstract void join(int port);
	public abstract void leave();
	public abstract void display(String s);
	/*@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}*/
	
}
