package wunderlist;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A token sent to the user in exchange of its name/password.
 * 
 * @author Paul
 * 
 */
@XmlRootElement
public class Token {
	String name = "null";
	String token = "null";

	public Token() {
		super();
	}

	public Token(String name, String token) {
		super();
		this.name = name;
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
