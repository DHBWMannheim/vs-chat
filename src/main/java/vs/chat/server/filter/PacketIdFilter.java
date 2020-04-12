package vs.chat.server.filter;

import vs.chat.packets.Packet;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class PacketIdFilter implements Filter {

	@Override
	public boolean canActivate(final Packet packet, final ServerContext context, final ConnectionHandler handler) {
		return !context.getWarehouse().knowsPacket(packet.getId());
	}

	@Override
	public void postHandle(final Packet packet, final ServerContext context, final ConnectionHandler handler) {
		context.getWarehouse().savePacket(packet.getId());
	}
}
