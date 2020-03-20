package vs.chat.packets;

import java.util.Set;
import java.util.UUID;

import vs.chat.entities.Chat;

public class LoginSyncPacket implements Packet {

	public UUID userId;
	public Set<Chat> chats;
	public Set<UUID> userIds;
	
}
