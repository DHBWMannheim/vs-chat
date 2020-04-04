package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.packets.KeyEchangePacket;
import vs.chat.packets.Packet;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class KeyExchangeListener implements Listener<KeyEchangePacket, Packet> {

	@Override
	public Packet next(final KeyEchangePacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		var currentUser = handler.getConnectedToUserId();
		if (currentUser.isEmpty())
			return null;

		packet.origin = handler.getConnectedToUserId().get();
		var localConnection = context.getConnectionForUserId(packet.target);
		if (localConnection.isPresent()) {
			localConnection.get().pushTo(packet);
		}
		context.getBroadcaster().send(packet);// TODO prevent recursive broadcast
		return null;
	}

}
