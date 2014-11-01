package resources;

import java.util.ArrayList;
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
import database.OwnershipDatabase;
import wunderlist.Listw;

/**
 * Methods called when the user sends a request to me/tasks
 * 
 * @author Paul / Modification of the code found here :
 *         http://www.vogella.com/tutorials/REST/article.html
 * 
 */
@Path("me/lists")
public class ListsResource {

	@Context
	HttpServletRequest sr;

	// Allows to insert contextual objects into the class,
	// e.g. ServletContext, Request, Response, UriInfo
	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	/**
	 * Return the list of all the lists owned by the user authenticated by its
	 * token
	 * 
	 * @return List of the lists in JSON format
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Listw> getLists() {
		List<Listw> lists = new ArrayList<Listw>();
		lists = OwnershipDatabase.getLists((String) sr
				.getAttribute("logged_user"));
		return lists;
	}

	/**
	 * Add a new list owned by the user authenticated into the database
	 * 
	 * 
	 * @param title
	 *            Title of the list
	 * @return The new list with its ID to retrieve it directly in JSON format
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Listw newList(@FormParam("title") String title) {
		Listw newList=OwnershipDatabase.addList((String) sr.getAttribute("logged_user"), new Listw(title));
	if(newList!=null)
		return newList;
	else
		throw new RuntimeException("Error... List not stored.");
	}

}