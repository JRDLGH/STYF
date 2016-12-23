/**
 * 
 */
package chat.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Set;

/**
 * This model is used for the server and to stock all the information about the users
 * @author JRDN
 *
 */
public class Server extends Observable{
	private boolean connected = false;
	private ArrayList<ArrayList<String>> clients = new ArrayList<ArrayList<String>>();
	private Hashtable<String,ArrayList<Integer>> conversation = new Hashtable<String, ArrayList<Integer>>();
	private Hashtable<Integer, Socket> sockets = new Hashtable<Integer, Socket>();
	private boolean identified = false;
	
	public Server(){
	}

	/**Returns all the clients, connected and disconnect
	 * @return the clients
	 */
	public ArrayList<ArrayList<String>> getClients() {
		return clients;
	}

	/**
	 * @param clients the clients to set
	 */
	public void setClients(int id,ArrayList<String> client) {
		this.clients.add(id,client);
		setChanged();
        notifyObservers(); // Indique qu'un client s'est connecté
	}

	/**
	 * @return the conversation
	 */
	public Hashtable<String, ArrayList<Integer>> getConversation() {
		return conversation;
	}

	/**
	 * @param conversation the conversation to set
	 */
	public void setConversation(Hashtable<String, ArrayList<Integer>> conversation) {
		this.conversation = conversation;
	}

	/** Returns all the sockets from Hashtable
	 * @return the sockets
	 */
	public Hashtable<Integer, Socket> getSockets() {
		return sockets;
	}
	/**
	 * This method get the socket of the client specified.
	 * @param clientId, the client id
	 * @return the socket of the client
	 */
	public Socket getSocketOf(int clientId) {
		if(clientId >= 0){
			return sockets.get(clientId);
		}else{
			return null;
		}
	}

	/** Add a clientId and a socket inside the hashtable sockets
	 * @param clientId the id of the client to put as a key
	 * @param sockets the sockets to set for the client id
	 */
	public void setSockets(int clientId, Socket s) {
		this.sockets.put(clientId, s);
		//System.out.println(this.sockets); // TEST
	}
	/**
	 * This method inform about the server connection state
	 * @return the server state, true if connected, false if not
	 */
	public boolean isConnected() {
		return connected;
	}
	/**
	 * This method allow to specify the server state connection
	 * @param connected, boolean used as a state
	 * true if the server is connected,
	 * false if the server is disconnected
	 */
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	/**
	 * This method inform if last user is identified
	 * @return boolean true if identified, false if not
	 */
	public boolean isIdentified() {
		return identified;
	}
	/**
	 * This method set if the last user have been identified or not
	 * @param identified, boolean, true if identified, false if not
	 */
	public void setIdentified(boolean identified) {
		this.identified = identified;
	}
	/**
	 * This method get the username/name of the client
	 * @param clientId, the client id used to identify the client
	 * @return name, a String, the name of the client
	 */
	public String getClientName(int clientId){
		String name = "Unknown";
		if(clientId >= 0){
			name = clients.get(clientId).get(0);
		}
		return name;
	}
	/**
	 * This method set the connection state for a client.
	 * @param clientId, the client
	 * @param state, this string takes only 2 values:
	 * connected, if the client is connected
	 * disconnected, if the client is disconnected
	 */
	public void setClientState(int clientId, String state){
		if(state.equals("disconnected") || state.equals("connected")){
			ArrayList<String> client = clients.get(clientId);
			client.set(3, state);
			//setchanged ?
		}
	}
	/**
	 * This method get if the client is currently connected
	 * @param clientId, the client id specified
	 * @return String "connected" if the client is connected,
	 * 		   String "disconnected" if the client is disconnected
	 */
	public String getClientState(int clientId){
		String state = null;
		if(!clients.isEmpty() || clients.get(clientId) != null){
			state = clients.get(clientId).get(3);
		}
		return state;
	}
	/**
	 * Get a arraylist of connected clients.
	 * @return an arraylist of connected clients
	 */
	public ArrayList<String> getConnectedClients(){
		ArrayList<String> connected = new ArrayList<String>();
		if(!clients.isEmpty()){
			for(int i=0;i<clients.size();i++){
				if(getClientState(i).equals("connected")){
					connected.add(getClientName(i));
				}
			}
		}
		return connected;
	}
	/**
	 * This method returns all conversation names.
	 * @return names, a ArrayList composed of Strings that are conversations names.
	 */
	public ArrayList<String> getConversationsNames(){
		ArrayList<String> names = new ArrayList<String>();
		if(conversation.isEmpty()){
			names = null;
		}else{
			Set<String> keys = conversation.keySet(); // prend le set de clés, donc prend toutes les clés
			for(String key: keys){
					//du numéro de thread, alors c'est qu'il fait partie de la conversation nommée key
					names.add(key);
				}
		}
		return names;
	}
	/**
	 * This method create a conversation if the conversation doesn't exist.
	 * @param clientId, the creator of the conversation
	 * @param name, the name of the conversation to create
	 */
	public void createConversation(int clientId, String name){
		if(!isExisting(name)){
			ArrayList<Integer> threadsId = new ArrayList<Integer>();
			threadsId.add(clientId);
			//view.display("Conversation " + name + " déja crée? " + conversation.containsKey(name));
			conversation.put(name, threadsId);
			System.out.println("Conversation " + name + " created successfully"); // ATT
		}else{
			System.out.println("Conversation [" + name + "] already exist.");
		}
	}
	/**
	 * This method add a client to a conversation.
	 * @param clientId, the id of the client to add
	 * @param name, the name of the conversation
	 */
	public void addToConversation(int clientId, String name){ // prob ici
		if(!conversation.containsKey(name)){
			System.out.println("Déjà membre? Non, la conversation existe pas");
		}else{
			System.out.println("Déjà membre? " + conversation.get(name).indexOf(clientId));
		}
		if(isExisting(name)){
			if(conversation.get(name).indexOf(clientId) != -1){
				System.out.println("You are already a member of a conversation");
			}else{
				if(conversationOf(clientId) == null){ // si appartient à aucune conversation
					ArrayList<Integer> threads = new ArrayList<Integer>(conversation.get(name));
					System.out.println(name + " - Threads " + threads);
					threads.add(clientId);
					System.out.println("Thread " + clientId + " is already in" + name + "? "
							+ conversation.get(name).indexOf(clientId));
					conversation.put(name, threads);
				}
			}
		}else{
			System.out.println("This conversation doesn't exist and has been created just for you!");
			createConversation(clientId, name);
		}
		displayConversationMembers(name);
	}
	/**
	 * This method returns the conversation of a client.
	 * @param clientId, the client
	 * @return name, a String that represents the conversation of the client.
	 */
	public String conversationOf(int clientId){
		String name = null;
		if(!conversation.isEmpty()){
			//System.out.println("KEYS? " + conversation.keySet());
			//System.out.println("T:" + threadId);
			Set<String> keys = conversation.keySet(); // prend le set de clés, donc prend toutes les clés
			for(String key: keys){
				//System.out.println("KEY: " + key);
				if(conversation.get(key).indexOf(clientId) != -1){ // Si dans une des clés, on trouve l'indexe
					//du numéro de thread, alors c'est qu'il fait partie de la conversation nommée key
					name = key;
				}
			}
		}
		if(name == null){
			//System.out.println(clients.get(threadId).get(0) +  " ne fait partie d'aucune conversation.");
		}
		return name;
	}
	/**
	 * This method display all the members of a conversation.
	 * @param name, the conversation name.
	 */
	public void displayConversationMembers(String name){
		if(isExisting(name)){
			ArrayList<Integer> threads = new ArrayList<Integer>(conversation.get(name));
			if(threads.isEmpty()){
				System.out.println("Conversation vide.");
			}else{
				System.out.println("MEMBRE[" + name + "] : " + threads);
				Iterator<Integer> iterator = threads.iterator();
				int i =0;
				while (iterator.hasNext()) {
					int j =(int) iterator.next();
					System.out.println(name + " - Membre " + Integer.toString(i) + " - Thread: " + j
					+ " User: " + clients.get(j).get(0));
					i++;
				}
			}
		}
	}
	/**
	 * This method get the clients connected in a conversation.
	 * @param name, the conversation
	 * @return members, an ArrayList of Integer that represents each client connected
	 */
	public ArrayList<Integer> getMembersOf(String name){
		ArrayList<Integer> members = new ArrayList<Integer>(conversation.get(name));
		return members;
	}
	/**
	 * This method is used to remove a client from his current conversation.
	 * @param clientId, represents client to remove of his current conversation
	 */
	public void removeClient(int clientId){
		int index = indexOfClient(clientId);
		if(index != -1 && conversationOf(clientId) != null){
			//TODO maj de la valeur dans clients (index=1 = convers) et la mettre à "none"?
			conversation.get(conversationOf(clientId)).remove(indexOfClient(clientId)); // faut connaitre l'index
			/*System.out.println("Le membre: " + threadId + " à bien été retiré de la conversation"
					+ conversationOf(threadId));*/
		}else{
			System.out.println("Impossible de retirer le membre de la conversation.");
		}
	}
	/**
	 * This method find the position(index) of the client in his current conversation.
	 * @param clientId, the client
	 * @return index, an Integer that represents the client position inside his conversation.
	 */
	public int indexOfClient(int clientId){
		int index = -1; // pas trouvé
		if(conversationOf(clientId) != null){
			ArrayList<Integer> threads = new ArrayList<Integer>(conversation.get(conversationOf(clientId)));
			int i =0;
			Iterator<Integer> iterator = threads.iterator(); // Contient tous les id des clients(threads)
			while (iterator.hasNext()) {
				int j =(int) iterator.next();
				//System.out.println("Ce que vaut j : " + j);
				if(j == clientId){
					index = i;
					/*view.display("On a trouvé : " + j + " à l'index: " + i);
					view.display("Les conversations" + conversation);
					view.display("Dans la conversation " + conversationOf(threadId) + " se trouve " + threads);*/
				}
				i++;
			}
		}else{
			System.out.println("Client " + clientId + " is not a member of any conversation.");
		}
		
		return index;
	}
	/**
	 * This method returns if the conversation exist.
	 * @param name, the conversation
	 * @return exist, a boolean, true if exist
	 * 		   					 false if not
	 */
	public boolean isExisting(String name){
		boolean exist = false;
		if(name != "" && conversation.containsKey(name)){
			exist = true;
		}
		return exist;
	}
}
