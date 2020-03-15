package vs.chat.entities;

import java.util.Set;
import java.util.UUID;

import vs.chat.packets.CreateChatPacket;

public class Chat extends CreateChatPacket implements Comparable<Chat> {

	private final UUID id = UUID.randomUUID();
	
	public Chat(final UUID... userIds) {
		super(userIds);
	}

	@Override
	public int compareTo(Chat o) {
		return this.id.compareTo(o.getId());
	}

	public UUID getId() {
		return id;
	}

	public Set<UUID> getUsers() {// TODO
		return this.users;
	}

}
