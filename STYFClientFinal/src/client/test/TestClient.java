package client.test;

import client.controller.Controller;
import client.model.Client;
import client.view.Client_GUI;
import client.view.Console;

public class TestClient {
	public static void main(String[] args){
		//Creating client
		Client J = new Client();
		//Creating controller
		Controller ctrl = new Controller(J);
		Controller GUIController = new Controller(J);
		//Creating view
		Console cons = new Console(J,ctrl);
		Client_GUI gui = new Client_GUI(J, GUIController);
		//Liaison controlleur - vue
		ctrl.addView(cons);
		GUIController.addView(gui);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				try{
					new TestClient();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
