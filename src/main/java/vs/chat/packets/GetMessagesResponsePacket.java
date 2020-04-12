package vs.chat.packets;

import java.util.Set;
import java.util.UUID;

import vs.chat.entities.Message;

public class GetMessagesResponsePacket extends Packet {

	private static final long serialVersionUID = 7936380783683426446L;
	private final UUID chatId;
	private final Set<Message> messages;

	public GetMessagesResponsePacket(final UUID chatId, final Set<Message> messages) {
		this.chatId = chatId;
		this.messages = messages;
	}

	public UUID getChatId() {
		return chatId;
	}

	public Set<Message> getMessages() {
		return messages;
	}
}
