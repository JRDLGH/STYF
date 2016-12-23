package chat.model;


import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

/**
 * This class contains all the data and operation of the chat.
 * @author JRDN
 *
 */
// CHAQUE FOIS QU'UNE INFO EST MODIFIÉE SUR LE MODELE, ON APPELLE setChanged() et notifyObservers()
public class Model extends Observable{
	private int port; // Spécifie le port utilisé pour la communication.
	private String ip; //Spécifie l'adresse ip du destinataire.
	private Socket socket;
	
	public void setPort(int port){
		this.port = port;
		setChanged();
		notifyObservers();
	}
	public int getPort(){
		return port;
	}
	public void setIp(String ip){
		this.ip  = ip;
		setChanged();
		notifyObservers();
	}
	public String getIp(){
		return ip;
	}
	public void startConnection(){
		try{
			setSocket(new Socket(InetAddress.getLocalHost(),port));
			System.out.println("Client successfully connected");
		}catch(UnknownHostException e){
			System.out.println(">> Erreur UnknowHost.");
		}catch(Exception e){
			System.out.println(">> An error occured.");
		}
	}
	public void stopConnection(){
		
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
