package vs.chat.entities;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

	private final UUID id;
	private String username;
	private String password;
	
	public User() {
		this.id = UUID.randomUUID();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UUID getId() {
		return id;
	}

}
