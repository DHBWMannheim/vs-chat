package vs.chat.server.listener;

import java.io.IOException;
import java.util.stream.Collectors;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.entities.User;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSyncPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class LoginListener implements Listener<LoginPacket, LoginSyncPacket> {

	@Override
	public LoginSyncPacket next(final LoginPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		// TODO Password / User prüfen
		System.out.println("Invoked LoginListener");

		context.getWarehouse().get(WarehouseResourceType.USERS).values().stream()
				.filter(u -> ((User) u).getUsername().equals(packet.username)).findFirst()
				.ifPresentOrElse(storedUser -> {
					var id = storedUser.getId();
					handler.setConnectedToUserId(id);
					System.out.println("connected id: " + id);
				}, () -> {
					System.out.println("writing new user");
					var user = new User();
					var id = user.getId();
					user.setUsername(packet.username);
					user.setPassword(packet.password);

					context.getWarehouse().get(WarehouseResourceType.USERS).put(id, user);
					System.out.println("created user with id:" + id);
					context.getBroadcaster().send(packet);
					handler.setConnectedToUserId(id);
				});

		var syncPacket = new LoginSyncPacket();
		syncPacket.userId = handler.getConnectedToUserId().get();
		syncPacket.chats = context.getWarehouse().get(WarehouseResourceType.CHATS).values().stream()
				.map(chat -> (Chat) chat)
				.filter(chat -> chat.getUsers().contains(syncPacket.userId))
				.collect(Collectors.toSet());
		syncPacket.userIds = context.getWarehouse().get(WarehouseResourceType.USERS).keySet();

		return syncPacket;
	}

}
