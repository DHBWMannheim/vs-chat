package vs.chat.server.listener;

import vs.chat.entities.Message;
import vs.chat.packets.MessagePacket;
import vs.chat.packets.MessageSuccessPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class MessageListener implements Listener<MessagePacket, MessageSuccessPacket> {

	@Override
	public MessageSuccessPacket next(final MessagePacket packet, final ServerContext context,
			final ConnectionHandler handler) {
		

		var newMessage = new Message();
		newMessage.setTarget(packet.target);
		newMessage.setOrigin(handler.getConnectedToUserId());
		newMessage.setContent(packet.content);
//		newMessage.setId(context.getWarehouse().getMessages().size());// TODO

		var storedMessage = context.getWarehouse().getMessages().stream().filter(m -> m.equals(newMessage)).findFirst();
		if (storedMessage.isPresent())
			return null;

		System.out.println("found a new message with target " + packet.target);
		var localConnection = context.getConnectionForUserId(packet.target);
		if (localConnection.isPresent()) {
			localConnection.get().pushTo(packet);
		}
		System.out.println("_____received message start_____");
		System.out.println(newMessage.getContent());
		System.out.println("_____received message end_____");
		context.getWarehouse().getMessages().add(newMessage);
		context.getBroadcaster().send(packet);
		return new MessageSuccessPacket();
	}

}
