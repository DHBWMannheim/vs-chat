package vs.chat.entities;

import java.util.UUID;

import vs.chat.server.warehouse.Warehouseable;

public class User implements Comparable<User>, Warehouseable {

	private final UUID id;
	private String username;
	private transient String password;
	
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
	
	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj);
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public int compareTo(final User o) {
		return this.id.compareTo(o.id);
	}

}
