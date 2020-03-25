package vs.chat.entities;

import java.util.UUID;

import vs.chat.packets.CreateChatPacket;
import vs.chat.server.warehouse.Warehouseable;

public class Chat extends CreateChatPacket implements Comparable<Chat>, Warehouseable {

	private final UUID id = UUID.randomUUID();

	public Chat(final String name, final UUID... userIds) {
		super(name, userIds);
	}

	@Override
	public int compareTo(Chat o) {
		return this.id.compareTo(o.getId());
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj);
	}
}
