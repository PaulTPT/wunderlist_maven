package resources;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import database.OwnershipDatabase;
import wunderlist.Task;

/**
 * Methods called when the user sends a request to me/tasks
 * 
 * @author Paul / Modification of the code found here :
 *         http://www.vogella.com/tutorials/REST/article.html
 * 
 */
@Path("me/tasks")
public class TasksResource {

	@Context
	HttpServletRequest sr;

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	/**
	 * Return the list of all the tasks owned by the user authenticated by its
	 * token
	 * 
	 * @return List of the tasks in JSON format
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> getTodos() {
		List<Task> todos = new ArrayList<Task>();
		todos = OwnershipDatabase.getTodos((String) sr
				.getAttribute("logged_user"));
		return todos;
	}

	/**
	 * Add a new task owned by the user authenticated into the database
	 * 
	 * 
	 * @param title
	 *            Title of the task
	 * @param list_id
	 *            Id of the list the task is part of
	 * @return The new task with its ID to retrieve it directly in JSON format
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Task newTodo(@FormParam("title") String title,
			@FormParam("list_id") int list_id,
			@FormParam("due_date") @DefaultValue("null") String due_date) {
		Task newTask = OwnershipDatabase.addTask((String) sr
				.getAttribute("logged_user"),
				new Task(title, due_date, list_id));
		ArrayList<String> listOwners = OwnershipDatabase.getListOwners(list_id);
		for (String owner : listOwners) {
			if (!owner.equals((String) sr.getAttribute("logged_user"))) {
				OwnershipDatabase.addSharedTask(owner, newTask);

			}
		}

		if (newTask != null)
			return newTask;
		else
			throw new RuntimeException("Error... List not stored.");

	}

}