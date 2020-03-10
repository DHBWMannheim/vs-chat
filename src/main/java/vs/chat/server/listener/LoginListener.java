package vs.chat.server.listener;

import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSuccessPacket;

public class LoginListener implements Listener<LoginPacket, LoginSuccessPacket>{

	@Override
	public LoginSuccessPacket next(LoginPacket packet) {
		//TODO Password / User prüfen
		System.out.println("Invoked LoginListener");
		return new LoginSuccessPacket();
	}

}
