package vs.chat.packets;

import java.util.UUID;

import vs.chat.server.warehouse.Warehouseable;

public abstract class Packet implements Warehouseable {

	private static final long serialVersionUID = -8665220609423477448L;
	private final UUID ID;

	protected Packet() {
		System.out.println("Creating packets");
		this.ID = UUID.randomUUID();
	}

	@Override
	public UUID getId() {
		return this.ID;
	}

}
