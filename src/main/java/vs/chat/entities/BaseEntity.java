package vs.chat.entities;

import java.util.UUID;

import vs.chat.server.warehouse.WarehouseResourceType;
import vs.chat.server.warehouse.Warehouseable;

//TODO filter like packet filter
public class BaseEntity implements Comparable<BaseEntity>, Warehouseable {

	private final UUID id;
	private final WarehouseResourceType type;

	public BaseEntity(final WarehouseResourceType type) {
		this.id = UUID.randomUUID();
		this.type = type;
	}

	@Override
	public boolean equals(Object obj) {
		return this.id.equals(obj);
	}

	@Override
	public UUID getId() {
		return id;
	}

	public WarehouseResourceType getType() {
		return type;
	}

	@Override
	public int compareTo(final BaseEntity o) {
		return this.id.compareTo(o.getId());
	}
}
