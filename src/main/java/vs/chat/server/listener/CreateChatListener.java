package vs.chat.server.listener;

import java.io.IOException;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.packets.BaseEntityBroadcastPacket;
import vs.chat.packets.CreateChatPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class CreateChatListener implements Listener<CreateChatPacket, BaseEntityBroadcastPacket> {

	@Override
	public BaseEntityBroadcastPacket next(final CreateChatPacket packet, final ServerContext context,
			final ConnectionHandler handler) throws IOException {
		var currentUser = handler.getConnectedToUserId();

		if (currentUser.isEmpty())
			return null;

		packet.getUsers().add(currentUser.get());

		UUID[] myArray = new UUID[packet.getUsers().size()];
		packet.getUsers().toArray(myArray);
		Chat newChat = new Chat(packet.getName(), myArray);

		context.getWarehouse().get(WarehouseResourceType.CHATS).put(newChat.getId(), newChat);

		var broadcastPacket = new BaseEntityBroadcastPacket();
		broadcastPacket.baseEntity = newChat;
		context.getBroadcaster().send(broadcastPacket);
		
		for(var user: packet.getUsers()) {
			var localConnection = context.getConnectionForUserId(user);
			if (localConnection.isPresent()) {
				localConnection.get().pushTo(broadcastPacket);
			}
		}
		return null;
	}

}
