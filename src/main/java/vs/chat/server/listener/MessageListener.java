package vs.chat.server.listener;

import vs.chat.packets.MessagePacket;
import vs.chat.packets.MessageSuccessPacket;

public class MessageListener implements Listener<MessagePacket, MessageSuccessPacket> {

	@Override
	public MessageSuccessPacket next(MessagePacket packet) {
		System.out.println("_____received message start_____");
		System.out.println(packet.content);
		System.out.println("_____received message end_____");
		return new MessageSuccessPacket();
	}

}
