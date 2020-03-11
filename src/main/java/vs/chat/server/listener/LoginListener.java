package vs.chat.server.listener;

import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSuccessPacket;
import vs.chat.server.ServerContext;

public class LoginListener implements Listener<LoginPacket, LoginSuccessPacket> {

	@Override
	public LoginSuccessPacket next(final LoginPacket packet, final ServerContext context) {
		// TODO Password / User prüfen
		System.out.println("Invoked LoginListener");
		return new LoginSuccessPacket();
	}

}
