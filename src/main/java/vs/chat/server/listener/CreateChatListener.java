package vs.chat.server.listener;

import java.io.IOException;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.packets.CreateChatPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class CreateChatListener implements Listener<CreateChatPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final CreateChatPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {

		Chat newChat;
		if (packet instanceof Chat) {
			newChat = (Chat) packet;
			var storedChats = context.getWarehouse().getChats().stream().filter(m -> m.getId().equals(newChat.getId()))
					.findFirst();
			if (storedChats.isPresent())
				return null;
		} else {
			newChat = new Chat((UUID[]) packet.getUsers().toArray());
		}

		System.out.println("found a new chat");

		context.getWarehouse().getChats().add(newChat);
		context.getBroadcaster().send(newChat);

		return new NoOpPacket();
	}

}
