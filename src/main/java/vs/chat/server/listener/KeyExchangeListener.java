package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.packets.KeyExchangePacket;
import vs.chat.packets.Packet;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class KeyExchangeListener implements Listener<KeyExchangePacket, Packet> {

	@Override
	public Packet next(final KeyExchangePacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		var currentUser = handler.getConnectedToUserId();
		if (currentUser.isPresent()) {
			packet.setOrigin(handler.getConnectedToUserId().get());
		}
		if (null == packet.getOrigin()) {
			return null;
		}

		var localConnections = context.getConnectionForUserId(packet.getTarget());
		for (var connection : localConnections) {
			connection.pushTo(packet);
		}
		context.getBroadcaster().send(packet);
		return null;
	}

}
