package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.packets.BaseEntityBroadcastPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class BaseEntityBroadcastListener implements Listener<BaseEntityBroadcastPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final BaseEntityBroadcastPacket packet, final ServerContext context,
			final ConnectionHandler handler) throws IOException {

		var entity = packet.getBaseEntity();

		var exists = context.getWarehouse().get(entity.getType()).containsKey(entity.getId());
		if (!exists) {
			context.getWarehouse().get(entity.getType()).put(entity.getId(), entity);
			context.getBroadcaster().send(packet);
		}

		return null;
	}

}
