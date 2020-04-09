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

	public Message(final UUID origin, final Date receiveTime) {
		super(WarehouseResourceType.MESSAGES);
		this.origin = origin;
		this.receiveTime = receiveTime;
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

	@Override
	public int compareTo(final BaseEntity obj) {
		if (obj instanceof Message) {
			var compareResult = this.receiveTime.compareTo(((Message) obj).getReceiveTime());
			if (compareResult != 0)
				return compareResult;
		}
		return super.compareTo(obj);
	}

}
