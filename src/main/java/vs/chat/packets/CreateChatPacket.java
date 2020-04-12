package vs.chat.packets;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class CreateChatPacket extends Packet {

	private static final long serialVersionUID = 6427674782435562886L;
	private final String name;
	private final Set<UUID> users = Collections.synchronizedSet(new TreeSet<>());

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
