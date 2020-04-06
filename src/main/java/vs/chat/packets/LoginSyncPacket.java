package vs.chat.packets;

import java.util.Set;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.entities.User;

public class LoginSyncPacket extends Packet {

	public UUID userId;
	public Set<Chat> chats;
	public Set<User> users;	
}
