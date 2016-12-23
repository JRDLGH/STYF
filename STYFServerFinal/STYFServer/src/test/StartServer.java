package test;

import chat.controller.ServerController;
import chat.model.Server;
import chat.view.Console;
import chat.view.ServerGUI;

public class StartServer {
	public static void main(String[] args){
Server myServer = new Server(); // Cr�ation du mod�le Serveur
		
		
		//Cr�ation des controleurs : un pour chaque vue
		//Chaque controleur doit avoir une r�f�rence vers le modele pour pouvoir le commander 
		ServerController myController = new ServerController(myServer); // Cr�ation du controlleur
		ServerController GUIController = new ServerController(myServer);
		//ServerController LBController = new ServerController(myServer);
		//chaque vue doit connaitre son controleur et avoir une reference vers le modele pour pouvoir l'observer
		Console cons = new Console(myServer, myController);
		ServerGUI gui = new ServerGUI(myServer, GUIController);
		
		//On donne la r�f�rence a la vue pour chaque controlleur
		myController.addView(cons);
		GUIController.addView(gui);
		
		
	
	
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new StartServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
