package wunderlist;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.Status;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import database.UserDatabase;

/**
 * Authenticate the user
 * 
 * @author Paul / Modification of the code found here :
 *         http://simplapi.wordpress
 *         .com/2013/01/24/jersey-jax-rs-implements-
 *         a-http-basic-auth-decoder/
 *         
 */
public class AuthFilter implements ContainerRequestFilter {

	@Context
	HttpServletRequest sr;

	/**
	 *         Apply the filter : check input request, validate or not with user
	 *         authentication token
	 * @param containerRequest
	 *            The request from Tomcat server
	 */
	@Override
	public ContainerRequest filter(ContainerRequest containerRequest)
			throws WebApplicationException {

		// GET, POST, PUT, DELETE, ...
		String method = containerRequest.getMethod();

		// /tasks for example
		String path = containerRequest.getPath(true);

		System.out.println(path);

		// We allow login login and registration with POST method
		if ((path.equals("login") || path.equals("login/")
				|| path.equals("register") || path.equals("register/"))
				&& method.equals("POST")) {
			return containerRequest;
		}

		// Otherwise, we check the user token

		// Get the authentification token passed in HTTP headers parameters
		String auth = containerRequest.getHeaderValue("authorization");
		System.out.println(auth);

		// If the user does not have the right (does not provide any HTTP
		// Authentication token)
		if (auth == null) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		// If login or password fail
		if (UserDatabase.userFromToken(auth) == null) {
			throw new WebApplicationException(Status.UNAUTHORIZED);
		}

		// Allow the request and store the name of the user to be used later
		sr.setAttribute("logged_user", UserDatabase.userFromToken(auth));

		return containerRequest;
	}
}
