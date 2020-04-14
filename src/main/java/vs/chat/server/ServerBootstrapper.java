package vs.chat.server;

import vs.chat.server.node.NodeConfig;

public class ServerBootstrapper {

	public static void main(final String[] args) {
		var server = new Server(9876);//new NodeConfig("localhost", 9877)
		var mainThread = new Thread(server);
		mainThread.start();
	}
}
