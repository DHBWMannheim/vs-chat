package vs.chat.server;

public class ServerBootstrapper {

	public static void main(String[] args) {
		var server = new Server(9876,9877);
		var mainThread = new Thread(server);
		mainThread.start();
		
		var server2 = new Server(9877,9876);
		var mainThread2 = new Thread(server2);
		mainThread2.start();
	}
}
