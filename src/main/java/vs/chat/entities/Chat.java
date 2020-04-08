package vs.chat.entities;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import vs.chat.server.warehouse.WarehouseResourceType;

public class Chat extends BaseEntity {

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

}
