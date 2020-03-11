package vs.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vs.chat.packets.Packet;

public class BrotherNodeBroadcaster {

	private List<BrotherNodeConnection> nodes = new ArrayList<>();

	public BrotherNodeBroadcaster(String tempAddress, int port, ServerContext context) {// TODO multiple
		var conn = new BrotherNodeConnection(tempAddress, port, context);
		conn.start();
		nodes.add(conn);
	}

	public void send(final Packet packet) {
		for (var node : nodes) {
			node.send(packet);
		}
	}

	public void close() {
		for (var node : nodes) {
			try {
				node.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
