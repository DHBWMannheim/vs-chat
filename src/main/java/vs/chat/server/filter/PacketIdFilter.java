package vs.chat.server.filter;

import vs.chat.packets.Packet;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class PacketIdFilter implements Filter {

	@Override
	public boolean canActivate(final Packet packet, final ServerContext context, final ConnectionHandler handler) {
		return context.getWarehouse().get(WarehouseResourceType.PACKETS).containsKey(packet.getId());
	}

	@Override
	public void postHandle(final Packet packet, final ServerContext context, final ConnectionHandler handler) {
		context.getWarehouse().get(WarehouseResourceType.PACKETS).put(packet.getId(), packet);
	}
}
