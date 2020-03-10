package packets;

import java.io.Serializable;

public class LoginPacket implements Serializable, Packet {

	public String username;
	public String password;
}
