package vs.chat.packets;

import vs.chat.entities.BaseEntity;

public class BaseEntityBroadcastPacket extends Packet {

	private static final long serialVersionUID = -5610865395397391284L;
	private final BaseEntity baseEntity;

	public BaseEntityBroadcastPacket(final BaseEntity baseEntity) {
		this.baseEntity = baseEntity;
	}

	public BaseEntity getBaseEntity() {
		return baseEntity;
	}
}
