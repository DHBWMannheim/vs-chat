package vs.chat.server.listener;

import packets.LoginPacket;
import packets.LoginSuccessPacket;

@Listener(LoginPacket.class)
public class LoginListener implements PacketListener<LoginPacket, LoginSuccessPacket>{

	@Override
	public LoginSuccessPacket next(LoginPacket packets) {
		//TODO Password / User prüfen
		return new LoginSuccessPacket();
	}

}
