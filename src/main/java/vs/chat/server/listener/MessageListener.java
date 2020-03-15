package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.entities.Message;
import vs.chat.packets.MessagePacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class MessageListener implements Listener<MessagePacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final MessagePacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {

		Message newMessage;
		if (packet instanceof Message) {
			newMessage = (Message) packet;
			var storedMessage = context.getWarehouse().getMessages().stream()
					.filter(m -> m.getId().equals(newMessage.getId())).findFirst();
			if (storedMessage.isPresent())
				return null;
		} else {
			newMessage = new Message();
			newMessage.setTarget(packet.target);
			newMessage.setOrigin(handler.getConnectedToUserId());
			newMessage.setContent(packet.content);
		}

		System.out.println("found a new message with target " + newMessage.target);

		var correspondingChat = context.getWarehouse().getChats().stream()
				.filter(c -> c.getId().equals(newMessage.target)).findFirst();
		if (correspondingChat.isEmpty()) {
			// TODO throw error as the chat id is invalid
		}
		var chat = correspondingChat.get();

		for (var user : chat.getUsers()) {
			var localConnection = context.getConnectionForUserId(user);
			if (localConnection.isPresent()) {
				localConnection.get().pushTo(newMessage);
			}
		}

		context.getWarehouse().getMessages().add(newMessage);
		context.getBroadcaster().send(newMessage);

		return new NoOpPacket();
	}

}
