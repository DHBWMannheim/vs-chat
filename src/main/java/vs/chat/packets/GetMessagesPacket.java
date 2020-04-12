package vs.chat.packets;

import java.util.UUID;

public class GetMessagesPacket extends Packet{

	private static final long serialVersionUID = 4961464135666023264L;
	private final UUID chatId;
	
	public GetMessagesPacket(final UUID id) {
		this.chatId = id;
	}
	
	public UUID getChatId() {
		return chatId;
	}
}
