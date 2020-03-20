package vs.chat.packets;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class CreateChatPacket implements Packet {

	protected final String name;
	protected final Set<UUID> users = Collections.synchronizedSet(new TreeSet<>());

	public CreateChatPacket(final String name, final UUID... userIds) {
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
