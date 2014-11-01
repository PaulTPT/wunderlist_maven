package resources;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import database.CommentsDatabase;
import database.ListsDatabase;
import database.OwnershipDatabase;
import database.TasksDatabase;
import database.UserDatabase;
import wunderlist.Comment;
import wunderlist.Listw;
import wunderlist.Task;
import wunderlist.TaskList;

/**
 * Methods called when the user want to access/modify a task or a list with its
 * ID
 * 
 * @author Paul
 * 
 */
@Path("{id : [0-9]+}")
public class IdResource {

	@Context
	HttpServletRequest sr;

	public IdResource() {
	}

	/**
	 * Returns the list/task
	 * 
	 * @return The list/task in JSON format
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public TaskList get(@PathParam("id") int id) {
		if (OwnershipDatabase.userOwnsTask(
				(String) sr.getAttribute("logged_user"), id)) {
			return TasksDatabase.retrieveTodo(id);
		} else if (OwnershipDatabase.userOwnsList(
				(String) sr.getAttribute("logged_user"), id)) {
			return ListsDatabase.retrieveList(id);
		} else
			throw new RuntimeException("Get: Task/List with id " + id
					+ " not found or not owned by you");

	}

	/**
	 * Modify a task/list already existing and owned by the user by sending it
	 * in JSON format
	 * 
	 * @return The task/list modified in JSON
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public TaskList modify(@PathParam("id") int id,
			@FormParam("title") @DefaultValue("null") String title,
			@FormParam("list_id") @DefaultValue("0") int list_id) {

		if (OwnershipDatabase.userOwnsTask(
				(String) sr.getAttribute("logged_user"), id)) {

			Task formerTodo = TasksDatabase.retrieveTodo(id);

			if (title.equals("null"))
				title = formerTodo.getTitle();
			;

			if (list_id == 0)
				list_id = formerTodo.getList_id();

			Task updatedTodo = new Task(id, title, list_id);
			TasksDatabase.updateTodo(updatedTodo);
			return updatedTodo;
		} else if (OwnershipDatabase.userOwnsList(
				(String) sr.getAttribute("logged_user"), id)) {

			Listw formerList = ListsDatabase.retrieveList(id);

			if (title.equals("null"))
				title = formerList.getTitle();

			Listw updatedList = new Listw(id, title);
			ListsDatabase.updateList(updatedList);
			return updatedList;
		} else
			throw new RuntimeException("Put: Task/List with id " + id
					+ " not found or not owned by you");
	}

	/**
	 * Delete a task/list owned by the user
	 * 
	 * @return 200:OK or a RuntimeException if failed
	 */
	@DELETE
	public Response delete(@PathParam("id") int id) {
		if (OwnershipDatabase.userOwnsTask(
				(String) sr.getAttribute("logged_user"), id)) {
			OwnershipDatabase.removeTask(id);
			return Response.ok().build();

		} else if (OwnershipDatabase.userOwnsList(
				(String) sr.getAttribute("logged_user"), id)) {
			OwnershipDatabase.removeListFromUser(
					(String) sr.getAttribute("logged_user"), id);
			return Response.ok().build();
		} else {
			throw new RuntimeException("Delete: Task/List with id " + id
					+ " not found or not owned by you");
		}

	}

	@Path("/messages")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getComments(@PathParam("id") int task_id) {
		if (OwnershipDatabase.userOwnsTask(
				(String) sr.getAttribute("logged_user"), task_id)) {
			return CommentsDatabase.retrieveTaskComments(task_id);
		} else {
			throw new RuntimeException("Get comments: Task with id " + task_id
					+ " not found or not owned by you");
		}

	}

	@Path("/messages")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Comment addComment(@PathParam("id") int task_id,
			@FormParam("text") String text) {
		if (OwnershipDatabase.userOwnsTask(
				(String) sr.getAttribute("logged_user"), task_id)) {
			Comment newComment = CommentsDatabase.storeComment(new Comment(
					task_id, text));
			return newComment;
		} else {
			throw new RuntimeException("POST comments: Task with id " + task_id
					+ " not found or not owned by you");
		}

	}

	@Path("/shares")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Listw ShareList(@PathParam("id") int list_id,
			@FormParam("recipient") String recipient) {
		String[] parts=recipient.split("\"");
		recipient=parts[3];
		if (OwnershipDatabase.userOwnsList(
				(String) sr.getAttribute("logged_user"), list_id) && UserDatabase.UserExists(recipient)) {
			Listw list = ListsDatabase.retrieveList(list_id);
			
			try{
			OwnershipDatabase.shareListwithUser(recipient, list);
			}catch (Exception e){
				throw new RuntimeException("POST comments: List with id " + list_id
						+ " not found or not owned by you or recipient doesn't exist");
			}
			return list;
		} else {
			throw new RuntimeException("POST comments: List with id " + list_id
					+ " not found or not owned by you or recipient doesn't exist");
		}

	}

}