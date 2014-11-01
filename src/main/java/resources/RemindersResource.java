package resources;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import database.RemindersDatabase;
import wunderlist.Reminder;

/**
 * Methods called when the user sends a request to me/reminders
 * 
 * @author Paul
 * 
 */
@Path("me/reminders")
public class RemindersResource {

	@Context
	HttpServletRequest sr;

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	/**
	 * Return the list of all the reminders owned by the user authenticated by
	 * its token
	 * 
	 * @return List of the reminders in JSON format
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Reminder> getReminders() {
		return RemindersDatabase.retrieveRemindersFromUser((String) sr
				.getAttribute("logged_user"));
	}

	/**
	 * Add a new reminder owned by the user authenticated into the database
	 * 
	 * 
	 * @param task_id
	 *            Task the reminder is related to
	 * 
	 * @param date
	 *            Date associated with the reminder
	 * 
	 * @return The reminder in JSON format
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Reminder addReminder(@FormParam("task_id") int task_id,
			@FormParam("date") String date) {
		Reminder reminder = RemindersDatabase.storeReminder(new Reminder(
				task_id, date, (String) sr.getAttribute("logged_user")));
		if (reminder != null)
			return reminder;
		else
			throw new RuntimeException("Error... List not stored.");
	}
}