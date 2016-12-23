package client.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * This class contains methods to handle STYF database, to get
 * or to set value in our database.
 * For information, the database is composed of 2 columns:
 * 1.username, this string is the UNIQUE KEY, so it means, username
 * 	needs to be unique 
 * 2.password, this string, not encrypted, is contained in clear
 * in database and can't be null.
 * @author JRDN, CEDRIC
 *
 */
public class Database {
	private String url;;
	private String user;
	private String pwd;
	private Connection conn = null;
	private Statement state;
	/**
	 * This constructor directly connect the database.
	 */
	public Database(){
		connect();
	}
	/**
	 * This method connect the database and set its statement.
	 * The user need to have the postgresql Driver and the 
	 * local database available and created on his computer
	 * or it won't work and will throws Exceptions...
	 */
	public void connect(){
		try{
			Class.forName("org.postgresql.Driver");
			url  = "jdbc:postgresql://localhost:5432/STYF";
		    user = "postgres";
		    pwd  = "styf";
		    conn = DriverManager.getConnection(url, user, pwd);
		    state = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
		    		  ResultSet.CONCUR_UPDATABLE);
		    //Etablis la connection	grâce a la classe DriverManager
		}catch(ClassNotFoundException c){
			System.out.println("Could not find driver.");
		}catch(SQLException sql){
			System.out.println("Connection to database failed");
		}
	}
	/**
	 * This method verify if the username given in argument
	 * already exist or not in database. 
	 * @param username, the username to verify
	 * @return true if the username exist in database or
	 * 		   false if the username doesn't exist.
	 */
	public boolean isUniqueUsername(String username){
		boolean unique_username = false;
		ResultSet result = null;
		try {
			result = state.executeQuery("SELECT username FROM users WHERE username = '"+ username +"'");
			if(result.next()){
				unique_username = true;
			}
		} catch (SQLException e) {
			System.out.println("Error while querying the database!");
		}catch(NullPointerException n){
			System.out.println("Error state, statement not set.");
		}
		return unique_username;
	}
	/**
	 * This method create an (row)entry in database for the username given and password.
	 * If this entry, so if the username is already present in an entry it will 
	 * return an SQLException.
	 * @param username, the unique username to set as UNIQUE KEY entry in the row
	 * @param password, the password, can't be null or it will throws Exception
	 * @return null if method is successfully done or 
	 * 		   a string err, containing the error.
	 */
	public String subscription(String username, String password){
		String err = null;
		try {
			state.executeUpdate("INSERT INTO users " + "VALUES ('" +username+ "', '" +password+ "')");
		} catch (SQLException e) {
			err = "Username " +username+ " already exist";
		}
		return err;
	}
	/**
	 * This method verify the log information of the user:
	 * Verify if the username exist and if the password 
	 * correspond to username.
	 * @param username, the username of client
	 * @param password, the password
	 * @return err, the string is equals to null if no error,
	 * 		   		or is equal to the error.
	 */
	public String login(String username, String password){
		String err = null;
		String sql_pwd;
		try {
			if(isUniqueUsername(username)){
					ResultSet pass = null;
						pass = state.executeQuery("SELECT username FROM users WHERE password = '" 
								+ password + "' AND username = '"+username+"'");
						if(!pass.next()){
							err = "Wrong password!";
						}else{
							sql_pwd = pass.getString("username");
							if(sql_pwd.equals(username)){
								pass.close();
							}else{
								err = "Wrong password!";
							}
						}
					pass.close();
				}else{
					err="Incorrect username!";
				} 
		}catch (SQLException e) {
			err = "Error while querying the database!";
		} 
		return err;
	}
}
