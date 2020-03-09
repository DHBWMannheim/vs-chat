package vs.chat.server;

import java.io.Serializable;

public class Client implements Serializable, IIdentifiable {

	private static final long serialVersionUID = 8354369975145163402L;
	private final Integer id;
	private final String username;// Kann auch statt id genommen werden
	private final String password;

	public Client(Integer id, String username, String password) {
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return "" + id + "," + username + "," + password;
	}
}
