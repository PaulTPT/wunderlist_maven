package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wunderlist.Reminder;

/**
 * Methods relative to the access to the reminders database
 * 
 * @author Paul
 * 
 */
public class RemindersDatabase {

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
	 * Store a new reminder into the database
	 * 
	 * @param reminder
	 *            The reminder to be stored
	 * @return The reminder stored with its ID
	 */
	public static Reminder storeReminder(Reminder reminder) {
		checkDB();

		String requete = "INSERT INTO REMINDERS (DATE,TASK_ID,OWNER) VALUES ('"
				+ reminder.getDate() + "'," + reminder.getTask_id() + ",'"
				+ reminder.getOwner() + "')";
		try {
			Statement stmt = connec.createStatement();
			stmt.executeUpdate(requete, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			reminder.setId(id);
			stmt.close();
			return reminder;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Retrieve the reminders relative to a task
	 * 
	 * @param TaskID
	 *            The id of the task
	 * @return The reminders
	 */
	public static List<Reminder> retrieveTaskReminders(int TaskID) {
		checkDB();
		List<Reminder> list = new ArrayList<Reminder>();

		try {
			String requete = "SELECT ID, DATE, OWWNER FROM REMINDERS WHERE TASK_ID= "
					+ TaskID;
			ResultSet res;
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			while (res.next()) {
				list.add(new Reminder(res.getInt(1), TaskID, res.getString(2),
						res.getString(3)));
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return list;
	}

	/**
	 * Retrieve all the reminders
	 * 
	 * @param user
	 *            The user owning the reminders to retrieve
	 * 
	 * @return The reminders
	 */
	public static List<Reminder> retrieveRemindersFromUser(String user) {
		checkDB();
		List<Reminder> list = new ArrayList<Reminder>();

		try {
			String requete = "SELECT ID,TASK_ID,DATE FROM REMINDERS WHERE OWNER='"
					+ user + "'";
			ResultSet res;
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			while (res.next()) {
				list.add(new Reminder(res.getInt(1), res.getInt(2), res
						.getString(3),user));
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return list;
	}

	/**
	 * Delete a reminder from its ID
	 * 
	 * @param reminderID
	 *            The ID of the reminder to be deleted
	 * @return Whether it was successful or not
	 */
	public static boolean deleteReminder(int reminderID) {
		checkDB();

		String requete = "DELETE FROM REMINDERS WHERE ID= " + reminderID;
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
