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

		var knownUsers = context.getWarehouse().get(WarehouseResourceType.USERS);
		var filteredUsers = packet.getUsers().stream().filter(u -> knownUsers.containsKey(u)).toArray(UUID[]::new);

		Chat newChat = new Chat(packet.getName(), filteredUsers);

		context.getWarehouse().get(WarehouseResourceType.CHATS).put(newChat.getId(), newChat);

		var broadcastPacket = new BaseEntityBroadcastPacket(newChat);
		context.getBroadcaster().send(broadcastPacket);

		for (var user : filteredUsers) {
			var localConnections = context.getConnectionForUserId(user);
			for (var connection : localConnections) {
				connection.pushTo(broadcastPacket);
			}
		}
		return null;
	}

}
