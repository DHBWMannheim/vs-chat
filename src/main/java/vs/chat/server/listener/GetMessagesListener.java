package vs.chat.server.listener;

import java.io.IOException;
import java.util.stream.Collectors;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.packets.GetMessagesPacket;
import vs.chat.packets.GetMessagesResponsePacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class GetMessagesListener implements Listener<GetMessagesPacket, GetMessagesResponsePacket> {

	@Override
	public GetMessagesResponsePacket next(final GetMessagesPacket packet, final ServerContext context,
			final ConnectionHandler handler) throws IOException {

		var currentUser = handler.getConnectedToUserId();
		if (currentUser.isEmpty())
			return null;

		var chatId = packet.chatId;
		var chat = (Chat) context.getWarehouse().get(WarehouseResourceType.CHATS).get(chatId);
		if (!chat.getUsers().contains(currentUser.get()))
			return null;

		var messages = context.getWarehouse().get(WarehouseResourceType.MESSAGES).values().stream()
				.map(m -> (Message) m).filter(m -> m.target.equals(chatId)).collect(Collectors.toSet());

		var response = new GetMessagesResponsePacket();
		response.chatId = chatId;
		response.messages = messages;
		return response;
	}

}
