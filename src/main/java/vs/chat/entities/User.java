package vs.chat.entities;

import vs.chat.server.warehouse.Warehouseable;

import java.util.UUID;

public class User implements Comparable<User>, Warehouseable {

	private final UUID id;
	private String username;
	
	public User() {
		this.id = UUID.randomUUID();
	}
	
	public User(final UUID id, final String username) {
		this.id = id;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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
