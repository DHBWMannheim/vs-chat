package vs.chat.server;

public class ServerBootstrapper {

	public static void main(String[] args) {

		var server = new Server();
		var mainThread = new Thread(server);
		mainThread.start();
	}
}
