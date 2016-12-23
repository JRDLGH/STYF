/**
 * 
 */
package chat.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import chat.model.Server;
import chat.view.ServerView;
/**
 * @author JRDN
 *
 */
public class ServerController {
	PrintWriter out = null;
	private ServerSocket serverSocket = null;
	private Date now;
	Server server;
	ServerView view;
	/**
	 * This constructor link the controller to the model.
	 * @param server, link the controller to server
	 */
	public ServerController(Server server){
		this.server = server; // On lie le contrôlleur au serveur
	}
	/**
	 * Link the view to this controller
	 * @param view
	 */
	public void addView(ServerView view){
		this.view = view; // On lie le contrôlleur à une vue donnée.
	}
	/**
	 * Return true if port is valid
	 * @param port
	 * @return
	 */
	public boolean isPortValid(int port){
		boolean isValid =false;
		if(port <=1024 || port> 65535){
			view.display("!Please enter a port between 1024 and 65535.");
		}else{
			startServer(port);
			if(server.isConnected()){
				isValid = true;
			}
		}
		return isValid;
	}
	public void listCommande(String msg){
		//String m = "";
		switch(msg){
		case "stop":stopServer();
			//leave = true;
			break;
		case "show u":showConnectedUsers();
			break;
		case "show c":showConversationsNames();
			break;
		case "help": showCommand();
			break;
		default:view.display("#? Command not found. Type help.");
		}
		
	}
	public void showCommand(){
		view.display("#>You will find all the command you can use in the following list");
		view.display("show u/c\n\tu users, show connected users\n\tc conversations, show active conversations name "
				+ ", the number of client connected in the conversation and show inactive conversations.");
		view.display("stop\n\t shutdown the server and all communications.");
	}
	/**
	 * Start the server with port specified.
	 * @param port, the port to connect the server.
	 */
	public void startServer(int port){
		boolean isStarted = false;
		if(view != null){
			if(!server.isConnected()){ //Si le serveur n'est pas actif
				try{
					serverSocket = new ServerSocket(port);
					isStarted = true;
					server.setConnected(isStarted);
					view.display("Server is waiting for a cient...");
					//Attendre qu'un client se connecte.
					Runnable acc = new AcceptClient(serverSocket);
					new Thread(acc).start();
				}catch(IOException io){
					view.display("!Port already used by another software!");
				}catch(SecurityException s){
					view.display("!Security failure on this port!");
				}catch(IllegalArgumentException i){
					view.display("!Please, enter a valid port number!");
				}
			}else{
				view.display("Serveur actif");
			}
		}else{
			view.display("Error: view is missing.");
		}
	}
	/**
	 * This tread turn in loop to accept the client if he respect de restrictions
	 * set by the method identify()
	 * @author JRDN
	 *
	 */
	private class AcceptClient implements Runnable{
		private ServerSocket serverSocket = null;
		private Socket clientSocket = null;
		private int clientId;
		/**
		 * In this constructor, we get the server Socket in order to accept a client.
		 * @param s, the server socket
		 */
		public AcceptClient(ServerSocket s){ //On lui passe le socket serveur pour pouvoir accepter le client
			this.serverSocket = s;
		}
		public void run(){
			try{
				while(true){
					clientSocket = serverSocket.accept();
					//On accepte le client.
					if(identify(clientId, clientSocket)){
						server.setSockets(clientId, clientSocket); // On met ses données dans le modèle
						Runnable rec = new Recieve(clientSocket, clientId);
						new Thread(rec).start();
						clientId++; // On incrémente le nb de clients
					}
				}
			}catch(IOException io){
				if(!server.isConnected()){
					view.display("Server shuted down by administrator");
				}else{
					view.display("Client number " + clientId + " has been refused.");
				}
			}
		}
	}
	/**
	 * This thread receive user messages and redistribute them for conversation.
	 * @author JRDN
	 *
	 */
	private class Recieve implements Runnable{
		private BufferedReader in =null;
		private Socket clientSocket = null;
		private int clientId;
		private String msg;
		private String finalMsg;
		private String disconnect;
		/**
		 * This constructor get
		 * @param s, the client socket used for communication
		 * @param clientId, the client id to recognize who is sending the message
		 */
		public Recieve(Socket s, int clientId){
			this.clientSocket = s;
			this.clientId = clientId;
		}
		public void run(){
			try{
				in= new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				while(true){
						disconnect = "";
						msg = "";
						msg= in.readLine();
						if(msg == null || msg == ""){
							disconnect = " left the conversation.";
						}
						finalMsg = buildMessage(msg);
						if(disconnect.isEmpty()){
								view.display(currentDate() + "[" + clientId + "] - " + server.getClientName(clientId) + "-> "
										+ server.conversationOf(clientId) + " : " + msg);
								sendTo(clientId,finalMsg);
						}else{
							sendTo(clientId,disconnect);
							break;
						}
					}
				stopClient(clientId,clientSocket);
			}catch(IOException io){
				stopClient(clientId,clientSocket);
			}
		}
	}
	/**
	 * Get the current date in hours and minutes
	 * @return time, string that represents hours and minutes
	 */
	public String currentDate(){
		String time;
		now = Calendar.getInstance().getTime();
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		time = format.format(now);
		return time;
	}
	/**
	 * This method returns a message with time
	 * @param msg, the message of the user
	 * @return the time in hours and minutes added to message
	 */
	public String buildMessage(String msg){
		String finalMsg;
		finalMsg = "[" + currentDate() + "]>" + " - " + msg;
		return finalMsg;
	}
	/**
	 * This method send a curstom message to a specified client.
	 * @param clientId, the client, used to find his socket
	 * @param msg, the message to send to the client
	 */
	public void sendTo(int clientId, String msg){
		String myconv = server.conversationOf(clientId);
		Iterator<Integer> iterator = server.getMembersOf(myconv).iterator();
		int i = -1;
		while (iterator.hasNext()) {
			i = (int)iterator.next();
			if(i != clientId){ // Empêche d'envoyer le message du client à lui même.
				Runnable s = new SendToConversation(clientId,
						server.getSocketOf(i),msg);
					new Thread(s).start();
			}
		}
	}
	/**
	 * This method display all the current users connected to the server.
	 */
	public void showConnectedUsers(){ // les affichers par conversations si possible!
		ArrayList<String> connected = new ArrayList<String>();
		if(!server.getClients().isEmpty()){
			connected = server.getConnectedClients();
			if(connected.size() > 0){
				view.display("**********************\nUser connected [" + connected.size() + "]\n"
					+ connected.toString());
			}else{
				view.display("All the client are disconnected.");
			}
		}else{
			view.display("There is no client connected.");
		}
	}
	/*
	 * This method shows the current conversation, active and inactive ones.
	 */
	public void showConversationsNames(){
		//Trier en fonction de, si = active, inactive
		int length = 0;
		ArrayList<String> active = new ArrayList<String>(); 
		ArrayList<String> inactive = new ArrayList<String>(); 
		ArrayList<String> names = server.getConversationsNames();
		if(names != null){
			for(int i=0;i<names.size();i++){
				length = server.getConversation().get(names.get(i)).size();
				if(length > 0){
					/*view.display("Active: " + names.get(i) + "[" +
							length+ "]"); //+nb de users dedans?*/
					active.add(names.get(i) + "[" + length + "]"); // MODIFIED
				}else{
					inactive.add(names.get(i));
				}
			}
			if(!active.isEmpty())view.display("***Actives***\n" + active.toString());
			if(!inactive.isEmpty())view.display("**Inactive***\n" + inactive.toString());
		}else{
			view.display("There is no conversation...");
		}
	}
	/**
	 * This method is called in order to receive the client information send from
	 * a specified client
	 * @param client, the client id used to identify the client
	 * @param clientSocket, the client socket used for the communication
	 */
	public boolean identify(int client, Socket clientSocket){
		ArrayList<String> cl = new ArrayList<String>();
		String[] getClient = {""};
		ObjectInputStream objIn;
		int error=0;
		String err ="";
		boolean success = false;
		try{
			objIn = new ObjectInputStream(clientSocket.getInputStream());
			Object clientInfo = objIn.readObject(); // on lit l'objet reçu
			getClient = (String[]) clientInfo; // On met les infos envoyées dans le tableau getClient
			for(String c: getClient){ // On parcout le tableau getClient contenant les infos envoyées
				if(c == "" || c == null){
					error++;
				}
			}
			if(getClient.length != 4){
				error++;
				view.display("!Identification message size is incorrect!");
			}
			if(error == 0){
				success = true;
				server.setIdentified(success); // client identifié
				getClient[2] = "true"; // Il est identifié
				for(int i=0;i<getClient.length;i++){
					cl.add(getClient[i]);
				}
				//0: USERNAME 1: Conversation 2: Identifié? 3: Connecté?
				server.setClients(client, cl); // On inscrit le client
				server.addToConversation(client, getClient[1]);
				view.display("TEST " + server.getClients());
			}else{
				err += "!Error in identification message!";
				view.display("!Error in identification message!");
			}
		}catch(IOException io){
			err += "Unable to identify the client!";
			view.display("Unable to identify the client!");
		}catch(ClassNotFoundException c){
			err += "!Error: Class not found exception!";
			view.display("!Error: Class not found exception!");
		}
		if(!success){
			unicastError(clientSocket, err);
			refuseClient(clientSocket);
		}
		return success;
	}
	/**
	 * This method refuse the client if he fails the identification in thread
	 * accept and close his socket.
	 * @param clientSocket, the socket to close
	 */
	public void refuseClient(Socket clientSocket){
		if(clientSocket != null){
			try{
				clientSocket.close();
				view.display(">>>Access refused for client socket: " + clientSocket );
			}catch(Exception e){
				view.display("!Unable to close the refused socket" + clientSocket);
			}
		}
	}
	/**
	 * This method close a socket for a given client.
	 * @param clientId, the client we want to disconnect
	 * @param clientSocket, the socket to stop
	 */
	public void stopClient(int clientId, Socket clientSocket){
		if(clientSocket != null && server.getClientState(clientId).equals("connected")){
			try{
				clientSocket.close();
				server.removeClient(clientId);
				server.setClientState(clientId, "disconnected"); // On indique que le client est déco
				if(server.isConnected()){
					view.display(server.getClientName(clientId) + " is now " 
							+ server.getClientState(clientId));
				}else{
					view.display(server.getClientName(clientId)+ " " + server.getClientState(clientId) 
						+ " by server.");
				}
			}catch(Exception e){
				view.display("!Communication failed to close!");
			}
		}
	}
	/**
	 * This method stop the server and close all client socket connected.
	 */
	public void stopServer(){
		try {
			server.setConnected(false);
			for(int i=0;i<server.getSockets().size();i++){ //Fermeture de tous les sockets
				stopClient(i,server.getSocketOf(i));
			}
			serverSocket.close();
			view.display("Server shutdown.");
		} catch (IOException e) {
			view.display("Server failed to close!");
		}
	}
	/**
	 * This method send a error to one client, 
	 * @param clientSocket, the socket 
	 * @param msg, the error to send
	 */
	public void unicastError(Socket clientSocket, String msg){
		if(clientSocket != null){
			String finalMsg = "SERVER: " + msg;
			try{ 
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
				out.println(finalMsg); // Refuser ces messages là venant des users!
				out.flush();
			}catch (IOException e){
				view.display("SERVER: Unable to warn client ");
			}
		}
	}
	/**
	 * This thread send a message to a specified conversation.
	 * @author JRDN
	 *
	 */
	private class SendToConversation implements Runnable{
		private int clientId = 0;
		private PrintWriter out = null;
		private Socket send = null;
		private Socket client = null;
		private String msg = null;
		private String finalMsg = null;
		/**
		 * This constructor get the clientId who send the message, his socket
		 * and his message
		 * @param clientId, the client id
		 * @param x, the socket
		 * @param msg, the message
		 */
		public SendToConversation(int clientId, Socket x, String msg){
			this.clientId = clientId;
			this.client = x;
			this.msg = msg;
		}
		public void run(){
			send = client;
			try {
				finalMsg = server.getClientName(clientId) + msg;
				out = new PrintWriter(send.getOutputStream()); 
				out.println(finalMsg);
				if(msg == "" || msg == null){
					view.display("Error");
				}
				out.flush();
			} catch (IOException e) {
				view.display("Error! I/O error occurs when sending message to " + server.getClientName(clientId));
			}
		}
	}
}