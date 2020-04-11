package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.packets.BaseEntityBroadcastPacket;
import vs.chat.packets.MessagePacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class MessageListener implements Listener<MessagePacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final MessagePacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {

		if (handler.getConnectedToUserId().isEmpty()) {
			return null;
		}

		Message newMessage = new Message(handler.getConnectedToUserId().get());
		newMessage.setTarget(packet.target);
		newMessage.setContent(packet.content);

		System.out.println("found a new message with target " + newMessage.getTarget());

		var correspondingChat = (Chat) context.getWarehouse().get(WarehouseResourceType.CHATS)
				.get(newMessage.getTarget());
		if (correspondingChat == null) {
			return null;
		}
		context.getWarehouse().get(WarehouseResourceType.MESSAGES).put(newMessage.getId(), newMessage);

		var broadcastPacket = new BaseEntityBroadcastPacket();
		broadcastPacket.baseEntity = newMessage;
		for (var user : correspondingChat.getUsers()) {
			var localConnection = context.getConnectionForUserId(user);
			if (localConnection.isPresent()) {
				localConnection.get().pushTo(broadcastPacket);
			}
		}
		context.getBroadcaster().send(broadcastPacket);

		return null;
	}

}
