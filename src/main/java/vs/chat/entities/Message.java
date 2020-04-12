package vs.chat.entities;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import vs.chat.server.warehouse.WarehouseResourceType;

public class Message extends BaseEntity {

	private static final long serialVersionUID = 4948796217378008473L;
	private UUID target;
	private String content;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(content, origin, receiveTime, target);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		return Objects.equals(content, other.content) && Objects.equals(origin, other.origin)
				&& Objects.equals(receiveTime, other.receiveTime) && Objects.equals(target, other.target);
	}

}
