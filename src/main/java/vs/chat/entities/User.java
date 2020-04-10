package vs.chat.entities;

import vs.chat.server.warehouse.WarehouseResourceType;

import java.util.UUID;

public class User extends BaseEntity {

	private String username;

	public User() {
		super(WarehouseResourceType.USERS);
	}

	public User(final UUID id, final String username) {
		super(WarehouseResourceType.USERS, id);
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
