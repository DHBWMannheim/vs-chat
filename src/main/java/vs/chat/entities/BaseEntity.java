package vs.chat.entities;

import java.util.Objects;
import java.util.UUID;

import vs.chat.server.warehouse.WarehouseResourceType;
import vs.chat.server.warehouse.Warehouseable;

public abstract class BaseEntity implements Comparable<BaseEntity>, Warehouseable {

	private static final long serialVersionUID = -7694834516790098602L;
	private final UUID id;
	private final WarehouseResourceType type;

	public BaseEntity(final WarehouseResourceType type) {
		this.id = UUID.randomUUID();
		this.type = type;
	}

	public BaseEntity(final WarehouseResourceType type, final UUID id) {
		this.id = id;
		this.type = type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		return Objects.equals(id, other.id) && type == other.type;
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
