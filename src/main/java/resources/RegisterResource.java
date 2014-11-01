package resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import database.UserDatabase;

/**
 * Methods called when the user wants to register
 * 
 * @author Paul
 * 
 */
@Path("register")
public class RegisterResource {

	/**
	 * Register a user into the database
	 * 
	 * @param mail
	 *            The user's identification name/email
	 * @param password
	 *            The user's password
	 * @return OK:200 or Runtime exception if failed
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response register(@FormParam("email") String mail,
			@FormParam("password") String password) {
		if (UserDatabase.addUser(mail, password)) {
			return Response.ok().build();
		} else {
			return Response.status(422).build();
		}

	}

}
