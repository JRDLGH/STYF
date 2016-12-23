package client.model;

import java.util.Observable;

/**
 * This model class contains all of the data of clients,
 * it means database values but also local value as the:
 * port, username, conversation, client state, database,
 * login state.
 * 
 * @author JRDN
 *
 */
public class Client extends Observable{
	private int port; // The port used for the communication
	private String username; // The username used to identify the person;
	private String conversation; // The id to identify the client, generate the id with the username and random nb
	private boolean isClientConnected = false;
	private Database db;
	private boolean isLogged=false;
	/**
	 * Get the port
	 * @return the port
	 */
	public Client(){
		this.db = new Database();
	}
	public int getPort() {
		return port;
	}
	/**
	 * Set the port
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * Get the username
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * This method returns if the username is unique
	 * @param username, the username
	 * @return true if the username is new and doesn't exist in database
	 * 		   false if the username already exist. 
	 */
	public boolean isUniqueUsername(String username){
		return db.isUniqueUsername(username);
	}
	/**
	 * This method calls the method login from Database class,
	 * this method log the client and return error if failed.
	 * @param username, the client username
	 * @param password, the client password
	 * @return error, the error that caused the failure
	 */
	public String logAndCheck(String username, String password){
		return db.login(username, password);
	}
	/**
	 * This method call the database method subscription in order to
	 * sign in a username in database.
	 * @param username, the new client to subscribe
	 * @param password, the password of the new client
	 * @return err, equals null if not errors during sql operation,
	 * 				equals the error returned by the database method
	 */
	public String subscribe(String username, String password){
		String err = db.subscription(username, password);
		return err;
	}
	/**
	 * Set the username of the client
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * Get the client
	 * @return the client connection state
	 */
	public boolean getIsClientConnected(){
		return isClientConnected;
	}
	/**
	 * Set if the client is connected
	 * @param c, the boolean to set,
	 * true if connected, false if not.
	 */
	public void setIsClientConnected(boolean c){
		this.isClientConnected = c;
		
	}
	/**
	 * This method returns the conversation of the client!
	 * @return conversation, String that represents the name of the
	 * conversation 
	 */
	public String getConversation() {
		return conversation;
	}
	/**
	 * This method set the conversation of the client.
	 * @param conversation
	 */
	public void setConversation(String conversation) {
		this.conversation = conversation; 
		setChanged();
        notifyObservers();
	}
	/**
	 * This method get the login state of the user.
	 * Is used as a verification in controller.
	 * @return the isLogged
	 */
	public boolean isLogged() {
		return isLogged;
	}
	/**
	 * this method set the login sate of the user.
	 * @param isLogged, the boolean to set
	 * true if client successfully loged
	 * false if failure.
	 */
	public void setLogged(boolean isLogged) {
		this.isLogged = isLogged;
	}
}
