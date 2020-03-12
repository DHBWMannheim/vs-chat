package vs.chat.server.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vs.chat.entities.User;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSuccessPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.persistance.PersistanceHandler;

public class LoginListener implements Listener<LoginPacket, LoginSuccessPacket> {

	@Override
	public LoginSuccessPacket next(final LoginPacket packet, final ServerContext context,
			final ConnectionHandler handler) {
		// TODO Password / User prüfen
		System.out.println("Invoked LoginListener");

		var storedUser = context.getWarehouse().getUsers().stream()
				.filter(u -> u.getUsername().equals(packet.username)).findFirst();

		int id = 0;
		if (!storedUser.isPresent()) {
			System.out.println("writing new user");
			var user = new User();

			id = context.getWarehouse().getUsers().size();// TODO
			user.setId(id);
			user.setUsername(packet.username);// TODO
			user.setPassword(packet.password);
			System.out.println("created user with id:" + id);
			context.getWarehouse().getUsers().add(user);
			context.getBroadcaster().send(packet);
		} else {
			id = storedUser.get().getId();// TODO
			System.out.println("connected id: " + id);

		}

		handler.setConnectedToUserId(id);// TODO
		return new LoginSuccessPacket();
	}

}
