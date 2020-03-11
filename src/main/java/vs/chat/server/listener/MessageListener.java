package vs.chat.server.listener;

import vs.chat.packets.BroadcastPacket;
import vs.chat.packets.MessagePacket;
import vs.chat.packets.MessageSuccessPacket;
import vs.chat.server.ServerContext;

public class MessageListener implements Listener<MessagePacket, MessageSuccessPacket> {

	@Override
	public MessageSuccessPacket next(final MessagePacket packet, final ServerContext context) {
		System.out.println("_____received message start_____");
		System.out.println(packet.content);
		System.out.println("_____received message end_____");
		var broadcast = new BroadcastPacket();
		broadcast.packet = packet;
		context.getBroadcaster().send(broadcast);
		return new MessageSuccessPacket();
	}

}
