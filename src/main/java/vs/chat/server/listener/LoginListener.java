package vs.chat.server.listener;

import packets.LoginPacket;
import packets.LoginSuccessPacket;

public class LoginListener implements Listener<LoginPacket, LoginSuccessPacket>{

	@Override
	public LoginSuccessPacket next(LoginPacket packets) {
		//TODO Password / User prüfen
		return new LoginSuccessPacket();
	}

}
