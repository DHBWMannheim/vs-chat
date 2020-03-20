package vs.chat.server.listener;

import java.io.IOException;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.packets.CreateChatPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class CreateChatListener implements Listener<CreateChatPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final CreateChatPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		
		var currentUser = handler.getConnectedToUserId();
		if(currentUser.isEmpty()) return null;
		
		//TODO prevent user from creating chats without him in it

		Chat newChat;
		if (packet instanceof Chat) {
			newChat = (Chat) packet;
			var storedChats = context.getWarehouse().get(WarehouseResourceType.CHATS).get(newChat.getId());
			if (storedChats != null)
				return null;
		} else {
			UUID[] myArray = new UUID[packet.getUsers().size()];
			packet.getUsers().toArray(myArray);
			newChat = new Chat(myArray);
		}

		System.out.println("found a new chat: " + newChat.getId());

		context.getWarehouse().get(WarehouseResourceType.CHATS).put(newChat.getId(), newChat);
		context.getBroadcaster().send(newChat);

		return new NoOpPacket();
	}

}
