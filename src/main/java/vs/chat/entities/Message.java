package vs.chat.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import vs.chat.packets.MessagePacket;
import vs.chat.server.warehouse.Warehouseable;

public class Message extends MessagePacket implements Comparable<Message>, Warehouseable {

	private final UUID id;
	private final UUID origin;
	private final LocalDateTime receiveTime;

	public Message(final UUID origin) {
		this.id = UUID.randomUUID();
		this.origin = origin;
		this.receiveTime = LocalDateTime.now();
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

	public UUID getTarget() {
		return target;
	}

	public void setTarget(UUID target) {
		this.target = target;
	}

	public LocalDateTime getReceiveTime() {
		return receiveTime;
	}

	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj);
	}

	@Override
	public int compareTo(Message o) {
		var compareResult = this.receiveTime.compareTo(o.getReceiveTime());
		if (compareResult == 0)
			compareResult = this.id.compareTo(o.id);
		return compareResult;
	}

}
