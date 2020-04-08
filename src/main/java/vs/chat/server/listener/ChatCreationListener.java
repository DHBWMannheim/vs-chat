package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.entities.Chat;
import vs.chat.packets.BaseEntityBroadcastPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class ChatCreationListener implements Listener<BaseEntityBroadcastPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final BaseEntityBroadcastPacket packet, final ServerContext context,
			final ConnectionHandler handler) throws IOException {

		var entity = packet.baseEntity;
		if (entity instanceof Chat) {
			var chat = (Chat) entity;
			for(var user: chat.getUsers()) {
				var localConnection = context.getConnectionForUserId(user);
				if (localConnection.isPresent()) {
					localConnection.get().pushTo(packet);
				}
			}
		}
		return null;
	}

}
