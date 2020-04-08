package vs.chat.packets;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import vs.chat.server.warehouse.WarehouseResourceType;
import vs.chat.server.warehouse.Warehouseable;

public class NodeSyncPacket extends Packet {

	private static final long serialVersionUID = 4968317570912928151L;
	public ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> warehouse;
}
