package vs.chat.entities;

import java.util.Date;
import java.util.UUID;

import vs.chat.server.warehouse.WarehouseResourceType;

public class Message extends BaseEntity {

	public UUID target;
	public String content;
	private final UUID origin;
	private final Date receiveTime;

	public Message(final UUID origin) {
		super(WarehouseResourceType.MESSAGES);
		this.origin = origin;
		this.receiveTime = new Date();
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

	public Date getReceiveTime() {
		return receiveTime;
	}

	public int compareTo(Message o) {// TODO is this colliding?
		var compareResult = this.receiveTime.compareTo(o.getReceiveTime());
		if (compareResult == 0)
			compareResult = this.getId().compareTo(o.getId());
		return compareResult;
	}

}
