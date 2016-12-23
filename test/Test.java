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
		//Cr�ation du mod�le
		Model m = new Model();
		//Cr�ation des contr�lleurs
		Controller ctrlC = new Controller(m);
		//Cr�ation des vues
		Console viewC = new Console(m,ctrlC);
		//On donne la ref � la vue pour chaque controlleur
		ctrlC.addView(viewC);
	}
}
