package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.UUID;
import javax.naming.*;
import javax.sql.*;

/**
 * Methods relative to the access to the users database
 * 
 * @author Paul
 * 
 */
public class UserDatabase {

	// public static final String address = "java:comp/env/jdbc/mydb";
	// public static Connection connec = null;

	public static Context ctx;
	public static DataSource ds;
	public static Connection connec;

	public static long ExpirationTime = 1000 * 60 * 30; // Token expires after
														// 30 minutes

	/**
	 * Check if the database is initialized and do it if it was not the case
	 */
	public static void checkDB() {
		if (connec == null)
			initDatabase();
	}

	/**
	 * Initialize the database
	 * 
	 * @return Whether the initialization was a success or not
	 */

	public static boolean initDatabase() {

		try {
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("java:comp/env/jdbc/TodoDatabase");
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		try {
			connec = ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Change a user's password
	 * 
	 * @param name
	 *            Name of the user
	 * 
	 * @param password
	 *            New password
	 * 
	 * @return Whether it was successful
	 */
	public static boolean updateUser(String name, String password) {
		checkDB();

		String requete = "UPDATE USERS SET PASSWORD = '" + password
				+ "' WHERE name= '" + name + "'";
		try {
			Statement stmt = connec.createStatement();
			stmt.executeUpdate(requete);
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	/**
	 * Delete a user
	 * 
	 * @param name
	 *            NAme of the user
	 * @return Whether it was successful
	 */
	public static boolean deleteUser(String name) {
		checkDB();

		String requete = "DELETE FROM USERS WHERE Name= '" + name + "'";
		try {
			Statement stmt = connec.createStatement();
			stmt.executeUpdate(requete);
			stmt.close();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static boolean UserExists(String name) {
		checkDB();

		// Check that the user doesn't already exist
		String requete = "SELECT NAME FROM USERS WHERE NAME= '" + name + "'";
		ResultSet res;

		try {
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			if (res.isBeforeFirst()) { // This user already exists
				stmt.close();
				return true;
			}else{
			stmt.close();
			return false;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}

		

	}

	/**
	 * Add a user
	 * 
	 * @param name
	 *            The user's name
	 * @param password
	 *            The user's password
	 * @return Whether it was successful
	 */
	public static boolean addUser(String name, String password) {
		checkDB();

		if (UserExists(name)) {
			return false;
		} else {

			String requete = "INSERT INTO USERS(NAME,PASSWORD) VALUES ('"
					+ name + "','" + password + "')";
			try {
				Statement stmt = connec.createStatement();
				stmt.executeUpdate(requete);
				stmt.close();
				return true;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}

	}

	/**
	 * Check if the user is known in the database and if its password is correct
	 * 
	 * @param name
	 *            Name of the user
	 * @param password
	 *            Password to check
	 * @return Whether the user successfully logged-in
	 */
	public static boolean validateUser(String name, String password) {
		checkDB();
		Boolean validated = false;
		try {
			String requete = "SELECT NAME, PASSWORD FROM USERS WHERE NAME= '"
					+ name + "'";
			ResultSet res;
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			res.next();
			if (res.getString(1).equals(name)
					&& res.getString(2).equals(password)) {
				validated = true;

			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return validated;
	}

	/**
	 * Checks if the token exists in the database, and returns the corresponding
	 * user
	 * 
	 * @param token
	 *            The token
	 * @return The user corresponding to the token / null if the token was not
	 *         found in the database
	 */
	public static String userFromToken(String token) {

		checkDB();
		token = token.replaceFirst("[B|b]earer ", "");
		String name = null;
		try {
			String requete = "SELECT NAME,EXPDATE FROM USERS WHERE TOKEN= '"
					+ token + "'";
			ResultSet res;
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			res.next();

			Long ExpDate = res.getLong(2);
			Long time = (new Date()).getTime();
			if (time < ExpDate) {
				name = res.getString(1);
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return name;

	}

	/**
	 * Generate a new token for a user and store it in the database
	 * 
	 * @param name
	 *            The user
	 * @return The new token
	 */
	public static String addTokenToUser(String name) {
		checkDB();
		Long ExpDate = (new Date()).getTime() + ExpirationTime;
		String token = UUID.randomUUID().toString();

		String requete = "UPDATE USERS SET TOKEN= '" + token + "' ,EXPDATE= "
				+ ExpDate + " WHERE name= '" + name + "'";
		try {
			Statement stmt = connec.createStatement();
			stmt.executeUpdate(requete);
			stmt.close();
			return token;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static Connection getConnection() {
		checkDB();
		return connec;
	}

}
