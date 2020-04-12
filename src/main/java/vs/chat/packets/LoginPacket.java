package vs.chat.packets;

public class LoginPacket extends Packet {

	private static final long serialVersionUID = 5932129959397922873L;
	private final String username;
	private final String password;

	public LoginPacket(final String username, final String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
