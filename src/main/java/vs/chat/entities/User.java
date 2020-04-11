package vs.chat.entities;

import vs.chat.server.warehouse.WarehouseResourceType;

import java.util.Objects;
import java.util.UUID;

public class User extends BaseEntity {

	private static final long serialVersionUID = -6072233695251396943L;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(username);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}

}
