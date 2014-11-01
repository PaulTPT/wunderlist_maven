package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import wunderlist.Comment;


/**
 * Methods relative to the access to the comments database
 * 
 * @author Paul
 * 
 */
public class CommentsDatabase {

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
	 * Store a new comment into the database
	 * 
	 * @param comment
	 *            The comment to be stored
	 * @return The comment stored with its ID
	 */
	public static Comment storeComment(Comment comment) {
		checkDB();

		String requete = "INSERT INTO COMMENTS(TEXT,TASK_ID) VALUES ('"
				+ comment.getText() + "'," + comment.getTask_id() + ")";
		try {
			Statement stmt = connec.createStatement();
			stmt.executeUpdate(requete, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			comment.setId(id);
			stmt.close();
			return comment;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}



	/**
	 * Retrieve the comments relative to a task
	 * 
	 * @param TaskID The id of the task
	 * @return The comments
	 */
	public static List<Comment> retrieveTaskComments(int TaskID) {
		checkDB();
		List<Comment> list = new ArrayList<Comment>();

		try {
			String requete = "SELECT ID, TEXT FROM COMMENTS WHERE TASK_ID= "
					+ TaskID;
			ResultSet res;
			Statement stmt = connec.createStatement();
			res = stmt.executeQuery(requete);
			while (res.next()){
			 list.add(new Comment(res.getInt(1),TaskID, res.getString(2)));
			}
			res.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

		return list;
	}

	/**
	 * Delete a comment from its ID
	 * 
	 * @param commentID
	 *            The ID of the comment to be deleted
	 * @return Whether it was successful or not
	 */
	public static boolean deleteComment(int commentID) {
		checkDB();

		String requete = "DELETE FROM COMMENTS WHERE ID= " + commentID;
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
