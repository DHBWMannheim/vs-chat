package vs.chat.packets;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class CreateChatPacket implements Packet {

	protected final Set<UUID> users = Collections.synchronizedSet(new TreeSet<>());

	public CreateChatPacket(final UUID... userIds) {
		this.users.addAll(users);
	}

	public Set<UUID> getUsers() {
		return users;
	}

}
