package vs.chat.packets;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import vs.chat.server.warehouse.Warehouse;
import vs.chat.server.warehouse.WarehouseResourceType;
import vs.chat.server.warehouse.Warehouseable;

public class NodeSyncPacket extends Packet {

	public ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> warehouse;
}
