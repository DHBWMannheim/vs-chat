package vs.chat.packets;

import java.util.Set;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.entities.User;

public class LoginSyncPacket extends Packet {

	private static final long serialVersionUID = -3613002285364890102L;
	public final UUID userId;
	public final Set<Chat> chats;
	public final Set<User> users;

	public LoginSyncPacket(final UUID userId, final Set<Chat> chats, final Set<User> users) {
		this.userId = userId;
		this.chats = chats;
		this.users = users;
	}
}
