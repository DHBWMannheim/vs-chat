package vs.chat.entities;

import java.util.UUID;

import vs.chat.packets.MessagePacket;
import vs.chat.server.warehouse.Warehouseable;

public class Message extends MessagePacket implements Comparable<Message>, Warehouseable {

	private final UUID id;
	private UUID origin;

	public Message() {
		this.id = UUID.randomUUID();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public UUID getOrigin() {
		return origin;
	}

	public void setOrigin(UUID origin) {
		this.origin = origin;
	}

	public UUID getTarget() {
		return target;
	}

	public void setTarget(UUID target) {
		this.target = target;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public int compareTo(Message o) {
		return this.id.compareTo(o.id);
	}

}
