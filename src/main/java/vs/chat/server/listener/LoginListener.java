package vs.chat.server.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vs.chat.entities.User;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSuccessPacket;
import vs.chat.server.ServerContext;
import vs.chat.server.persistance.PersistanceHandler;

public class LoginListener implements Listener<LoginPacket, LoginSuccessPacket> {

	static int i = 0;// TODO temp

	private final PersistanceHandler<List<User>> handler = new PersistanceHandler<>("Users");
	private List<User> knownUsers;//TODO sync -> store in Warehouse and sync whole warehouse
	
	public LoginListener() {
		try {
			this.knownUsers = this.handler.load();
		} catch (ClassNotFoundException | IOException e) {
			this.knownUsers = new ArrayList<>();
		}
		for(var user: knownUsers) {
			System.out.println("Loader User: " + user.getUsername());
		}
	}

	@Override
	public LoginSuccessPacket next(final LoginPacket packet, final ServerContext context) {
		// TODO Password / User prüfen
		System.out.println("Invoked LoginListener");
		
		boolean isUserKnown = knownUsers.stream().anyMatch(u -> u.getUsername().equals(packet.username));
		if(!isUserKnown) {
			System.out.println("writing new user");
			var user = new User();
			user.setId(i++);//TODO
			user.setUsername(packet.username);//TODO
			user.setPassword(packet.password);
			this.knownUsers.add(user);
			try {
				this.handler.store(knownUsers);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		
		
		
		
		try {
			System.out.println("attempting to store user");
			handler.store(knownUsers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		context.getBroadcaster().send(packet);
		return new LoginSuccessPacket();
	}

}
