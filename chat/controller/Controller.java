/**
 * LAGACHE JORDAN GROUPE 16 
 */
package chat.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import chat.model.Model;
import chat.view.View;

/**
 * The controller handle interaction between the view and the model.
 * @author JRDN
 *
 */
public class Controller {
	//CHAQUE VUE DISPOSE DE SON PROPRE CONTROLLEUR
	private Model model;
	private View view = null;
	private ServerSocket s = null;
	private ServerSocket server = null;
	private Socket cs = null; // Not already (really) used
	private boolean serverStarted = false; //state of server
	private boolean clientStarted = false; //state of client
	
	/**
	 * Get the model.
	 * @param m, the model
	 */
	public Controller(Model m){
		model = m;
	}

	/**
	 * Verify if the port is correct and ready to use and
	 * finally set the port.
	 * @param port, the port to verify and if correct, to set as the port to use.
	 * @return true if the port is correct and set,
	 * false if the port is invalid.
	 */
	public boolean isPortValid(int port){
		if(port <= 0 || port > 65535){ // Check1: port are between 0 and 65535
			view.display(">> Invalid Port number.");
		}else{
			if(isActive(port)){ //Check2: port already used? 
				view.display(">> Port already used.");
				return false;
			}else{ //All is ok, ready to use the port
				model.setPort(port);
				//view.display(">> Value: " + isServerStarted());
				if(!isServerStarted()){
					view.join(model.getPort());
				}
				return true;
			}
		}
		return false; //if nothing done, just set to false
	}
	
	/**
	 * Verify if the port is available or not.
	 * @param port, the port to verify.
	 * @return true if the port is used,
	 * false if it's free to use.
	 */
	
	public boolean isActive(int port){
		boolean invalidPort = true;
		if(view != null){
			try{
				s = new ServerSocket(port);
			}catch(IOException e){
				view.display(">> I/O error occurs when opening the socket!");
				return invalidPort;
			}catch(SecurityException s){
				view.display(">> Security error!");
				return invalidPort;
			}catch(IllegalArgumentException i){
				view.display(">> Please enter a valid port number!");
				return invalidPort;
			}
			try {
				if(s != null){
					s.close();
					view.display(">> Port closed."); //pas besoin
				}
			} catch (IOException e1) {
				view.display(">> Port failed to close");
				return invalidPort;
			}
			invalidPort = false;
			return invalidPort;
		}else{
			view.display(">> No view");
			return invalidPort;
		}
	}
	
	/**
	 * This method start the server socket with the port specified.
	 * @param port, port used for the communication
	 */
	public void startServer(int port){
		view.display(">> Starting server on port: "  + port);
		if(!isActive(port)){
			try{
				server = new ServerSocket(port);
				//setSoTimeOut? A utiliser prochainement
				//setSoTimeOut(2000)? si pas d'info arrive en 2000ms, une exception de type
				//SocketTimeoutException est lancée
				setServerStarted(true);
				view.display(">>Listenning on Port : " + server.getLocalPort());
				/*cs = server.accept(); //On accepte la connection avec le client
				//Attend la connection d'un client avant de continuer.
				view.display(">> A client is connected.");*/
			}catch(Exception e){ //regroup SecurityException, IOException, IllegalArgumentException
				//e.printStackTrace();
				view.display(">> Server failed to start.");
				setServerStarted(false);
			}
		}else{
			view.display(">> Unable to start the server because the port " + port + " is already used.");
			setServerStarted(false);
		}
	}
	
	/**
	 * This method stop the server and verify if it is correctly done.
	 */
	public void stopServer(){
			if(server != null){
				try{
					server.close();
					if(server.isClosed()){
						view.display(">> Server successfully closed.");
					}else{
						view.display(">> Server is not closed.");
					}
				}catch(Exception e){
					view.display(">> Server failed to close.");
				}
			}else{
				view.display("The server doesn't exist!");
			}
	}

	/**
	 * Set the connection between the controller and the view.
	 * @param view, the view connected to the controller.
	 */
	public void addView(View view){
		this.view = view;
	}
	/**
	 * Define if the server is started or not.
	 * @return true if started, false if not
	 */
	public boolean isServerStarted() {
		return serverStarted;
	}
	/**
	 * Set the server state.
	 * @param serverStarted, the new state for the server.
	 */
	public void setServerStarted(boolean serverStarted) {
		this.serverStarted = serverStarted;
	}
	/**
	 * Define if the client is started or not.
	 * @return true if started, false if not
	 */
	public boolean isClientStarted() {
		return clientStarted;
	}
	/**
	 * Set the client state.
	 * @param clientStarted, the new state for the client
	 */
	public void setClientStarted(boolean clientStarted) {
		this.clientStarted = clientStarted;
	}
}
