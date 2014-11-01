package resources;

import java.io.IOException;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import wunderlist.Token;
import database.UserDatabase;

/**
 * 
 * Methods called when the user sends a request to /login
 * 
 * @author Paul
 * 
 */
@Path("/login")
public class LoginResource {

	/**
	 * Method called when a POST is received (the user tries to register to
	 * receive its token)
	 * 
	 * @param name
	 *            The name/email identifying the user
	 * @param password
	 *            The user's password
	 * @return The token of the user in JSON
	 * @throws IOException
	 *             If the user is not registered or if its password is wrong,
	 *             send an error 401 (unauthorized access)
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Token login(@FormParam("email") String name,
			@FormParam("password") String password) throws IOException {
		if (UserDatabase.validateUser(name, password)) {
			String token = UserDatabase.addTokenToUser(name);
			return new Token(name, token);
		} else {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

	}

}
