package vs.chat.server;

public class ServerBootstrapper {

	public static void main(String[] args) {
		var server = new Server(9877,9876);
		var mainThread = new Thread(server);
		mainThread.start();
		
	}
}
