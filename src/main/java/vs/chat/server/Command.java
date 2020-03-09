package vs.chat.server;

public enum Command {

	LOGIN("LOGIN"), SEND("SEND");

	public final String command;

	private Command(String command) {
		this.command = command;
	}
}
