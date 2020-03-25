package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.packets.MessagePacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class MessageListener implements Listener<MessagePacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final MessagePacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		
		if(handler.getConnectedToUserId().isEmpty()) {
			//TODO throw error as user is not authed
			return null;
		}

		Message newMessage;
		if (packet instanceof Message) {
			newMessage = (Message) packet;
			var storedMessage = context.getWarehouse().get(WarehouseResourceType.MESSAGES).get(newMessage.getId());
			if (storedMessage != null)
				return null;
		} else {
			newMessage = new Message(handler.getConnectedToUserId().get());
			newMessage.setTarget(packet.target);
			newMessage.setContent(packet.content);
		}

		System.out.println("found a new message with target " + newMessage.target);

		var correspondingChat = (Chat) context.getWarehouse().get(WarehouseResourceType.CHATS).get(newMessage.target);
		if (correspondingChat == null) {
			// TODO throw error as the chat id is invalid
			return new NoOpPacket();
		}
		for (var user : correspondingChat.getUsers()) {
			var localConnection = context.getConnectionForUserId(user);
			if (localConnection.isPresent()) {
				localConnection.get().pushTo(newMessage);
			}
		}

		context.getWarehouse().get(WarehouseResourceType.MESSAGES).put(newMessage.getId(), newMessage);
		context.getBroadcaster().send(newMessage);

		return null;
	}

}
