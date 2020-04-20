package vs.chat.server;

public class ServerBootstrapper {

	public static void main(final String[] args) {
		var server = new Server(9876);// new NodeConfig("host", port)
		var mainThread = new Thread(server);
		mainThread.start();
	}
}
