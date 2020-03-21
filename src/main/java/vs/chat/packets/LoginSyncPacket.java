package vs.chat.packets;

import java.util.Set;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;

public class LoginSyncPacket implements Packet {

	public UUID userId;
	public Set<Chat> chats;
	public Set<UUID> userIds;
	public Set<Message> messages;
	
}
