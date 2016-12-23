package test;

import chat.controller.Controller;
import chat.model.Model;
import chat.view.Console;

/**
 * TEST
 * @author JRDN
 *
 */
public class Test {
	public static void main(String[] args){
		//Création du modèle
		Model m = new Model();
		//Création des contrôlleurs
		Controller ctrlC = new Controller(m);
		//Création des vues
		Console viewC = new Console(m,ctrlC);
		//On donne la ref à la vue pour chaque controlleur
		ctrlC.addView(viewC);
	}
}
