package vs.chat.entities;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import vs.chat.server.warehouse.WarehouseResourceType;

public class Chat extends BaseEntity {

	private static final long serialVersionUID = -4542886655176666941L;
	private final String name;
	private final Set<UUID> users = Collections.synchronizedSet(new TreeSet<>());

	public Chat(final String name, final UUID... userIds) {
		super(WarehouseResourceType.CHATS);
		this.name = name;
		for (var id : userIds) {
			this.users.add(id);
		}
	}

	public Set<UUID> getUsers() {
		return users;
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(name, users);
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
		Chat other = (Chat) obj;
		return Objects.equals(name, other.name) && Objects.equals(users, other.users);
	}

}
