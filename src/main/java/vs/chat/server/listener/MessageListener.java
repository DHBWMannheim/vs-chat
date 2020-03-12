package vs.chat.server.listener;

import vs.chat.packets.MessagePacket;
import vs.chat.packets.MessageSuccessPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class MessageListener implements Listener<MessagePacket, MessageSuccessPacket> {

	@Override
	public MessageSuccessPacket next(final MessagePacket packet, final ServerContext context,
			final ConnectionHandler handler) {
		System.out.println("_____received message start_____");
		System.out.println(packet.content);
		System.out.println("_____received message end_____");
		context.getBroadcaster().send(packet);

		var localConnection = context.getConnectionForUserId(packet.target);
		if (localConnection.isPresent()) {
			localConnection.get().pushTo(packet);
		}

		return new MessageSuccessPacket();
	}

}
