package vs.chat.server.listener;

import java.io.IOException;
import java.util.stream.Collectors;

import vs.chat.entities.Chat;
import vs.chat.entities.PasswordUser;
import vs.chat.entities.User;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSyncPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.packets.Packet;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class LoginListener implements Listener<LoginPacket, Packet> {

	@Override
	public Packet next(final LoginPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		System.out.println("Invoked LoginListener");

		var res = context.getWarehouse().get(WarehouseResourceType.USERS).values().stream()
				.filter(u -> ((PasswordUser) u).getUsername().equals(packet.username)).findFirst();
		if (res.isPresent()) {
			if (!((PasswordUser) res.get()).hasPassword(packet.password)) {
				return new NoOpPacket();
			} else {
				var id = res.get().getId();
				handler.setConnectedToUserId(id);
				System.out.println("connected id: " + id);
			}
		} else {
			System.out.println("writing new user");
			var user = new PasswordUser();
			var id = user.getId();
			user.setUsername(packet.username);
			user.setPassword(packet.password);

			context.getWarehouse().get(WarehouseResourceType.USERS).put(id, user);
			System.out.println("created user with id:" + id);
			context.getBroadcaster().send(packet);
			handler.setConnectedToUserId(id);
		}
		var syncPacket = new LoginSyncPacket();
		syncPacket.userId = handler.getConnectedToUserId().get();
		syncPacket.chats = context.getWarehouse().get(WarehouseResourceType.CHATS).values().stream()
				.map(chat -> (Chat) chat).filter(chat -> chat.getUsers().contains(syncPacket.userId))
				.collect(Collectors.toSet());
		syncPacket.users = context.getWarehouse().get(WarehouseResourceType.USERS).values().stream().map(user -> {
			var u = (PasswordUser) user;
			return new User(u.getId(), u.getUsername());
		}).collect(Collectors.toSet());

		return syncPacket;
	}

}
