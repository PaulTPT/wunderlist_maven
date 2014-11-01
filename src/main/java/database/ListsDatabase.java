package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import wunderlist.Listw;

/**
 * Methods relative to the access to the lists database
 * 
 * @author Paul
 * 
 */
public class ListsDatabase {

	public static boolean DBInitialized = false;
	public static Connection connec = null;

	/**
	 * Check if the database is initialized and do it if it was not the case
	 */
	public static void checkDB() {
		while (!DBInitialized) {
			connec = UserDatabase.getConnection();
			if (connec != null)
				DBInitialized = true;
		}
	}

	/**
	 * Store a new list into the database
	 * 
	 * @param list
	 *            The list to be stored
	 * @return The list stored with its ID
	 */
	public static boolean storeList(Listw list) {
		checkDB();

		String requete = "INSERT INTO LISTS(ID,TITLE) VALUES (" + list.getId()
				+ ",'" + list.getTitle() + "')";
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
	 * Update a list
	 * 
	 * @param list
	 *            The list to be updated
	 * @return Whether the update was successful
	 */
	public static boolean updateList(Listw list) {
		checkDB();

		String requete = "UPDATE LISTS SET TITLE= '" + list.getTitle()
				+ "' WHERE ID = " + list.getId();
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
	 * Retrieve a list from its id
	 * 
	 * @param listID
	 *            The Id of the list to retrieve
	 * @return The list corresponding to the ID
	 */
	public static Listw retrieveList(int listID) {
		checkDB();

		Listw list = new Listw();

		try {
			String requete = "SELECT ID, TITLE FROM LISTS WHERE ID= " + listID;
			ResultSet res;
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			res.next();
			list = new Listw(res.getInt(1), res.getString(2));
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return list;
	}

	/**
	 * Delete a list from its ID
	 * 
	 * @param listID
	 *            The ID of the list to be deleted
	 * @return Whether it was successful or not
	 */
	public static boolean deleteList(int listID) {
		checkDB();

		String requete = "DELETE FROM LISTS WHERE ID= " + listID;
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

}
