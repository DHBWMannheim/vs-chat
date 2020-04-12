package vs.chat.server.node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import vs.chat.packets.Packet;
import vs.chat.server.ServerContext;

public class NodeBroadcaster {

	private final List<NodeConnection> nodes = new ArrayList<>();

	public NodeBroadcaster(final ServerContext context, final NodeConfig... configs) {
		for(var config: configs) {
			var conn = new NodeConnection(config.getAddress(), config.getPort(), context);
			conn.start();
			nodes.add(conn);			
		}
	}

	public void send(final Packet packet) {
		System.out.println("Broadcasting: " + packet);
		for (var node : nodes) {
			node.send(packet);
		}
	}

	public void close() {
		for (var node : nodes) {
			try {
				node.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
