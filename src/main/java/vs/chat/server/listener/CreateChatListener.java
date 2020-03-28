package vs.chat.server.listener;

import java.io.IOException;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.packets.CreateChatPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

//Falls Gruppenchats nicht erlaubt sind in der Basisversion kann diese Klasse gelöscht werden
public class CreateChatListener implements Listener<CreateChatPacket, Chat> {

	@Override
	public Chat next(final CreateChatPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		var currentUser = handler.getConnectedToUserId();

		Chat newChat;
		if (packet instanceof Chat) {
			// TODO prevent client from sending upper level packets
			newChat = (Chat) packet;
			var storedChats = context.getWarehouse().get(WarehouseResourceType.CHATS).get(newChat.getId());
			if (storedChats != null)
				return null;
		} else {
			if (currentUser.isEmpty())
				return null;

			packet.getUsers().add(currentUser.get());

			UUID[] myArray = new UUID[packet.getUsers().size()];
			packet.getUsers().toArray(myArray);
			newChat = new Chat(packet.getName(), myArray);
		}

		System.out.println("found a new chat: " + newChat.getId());

		context.getWarehouse().get(WarehouseResourceType.CHATS).put(newChat.getId(), newChat);
		context.getBroadcaster().send(newChat);

		return newChat;
	}

}
