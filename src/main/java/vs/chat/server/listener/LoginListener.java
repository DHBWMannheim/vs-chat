package vs.chat.server.listener;

import java.io.IOException;
import java.util.UUID;

import vs.chat.entities.User;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.WarehouseResourceType;

public class LoginListener implements Listener<LoginPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final LoginPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		// TODO Password / User prüfen
		System.out.println("Invoked LoginListener");

		var storedUser = context.getWarehouse().get(WarehouseResourceType.USERS).values().stream()
				.filter(u -> ((User) u).getUsername().equals(packet.username)).findFirst();

		UUID id;
		if (!storedUser.isPresent()) {

			System.out.println("writing new user");
			var user = new User();
			id = user.getId();
			user.setUsername(packet.username);
			user.setPassword(packet.password);
			System.out.println("created user with id:" + id);
			context.getWarehouse().get(WarehouseResourceType.USERS).put(id, user);
			context.getBroadcaster().send(packet);
		} else {
			id = storedUser.get().getId();
			System.out.println("connected id: " + id);
		}

		handler.setConnectedToUserId(id);
		return new NoOpPacket();
	}

}
