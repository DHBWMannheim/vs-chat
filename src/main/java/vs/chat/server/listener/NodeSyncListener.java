package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.packets.NoOpPacket;
import vs.chat.packets.NodeSyncPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class NodeSyncListener implements Listener<NodeSyncPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final NodeSyncPacket packet, final ServerContext context, final ConnectionHandler handler)
			throws IOException {
		System.out.println("cre");
		var needsBroadcast = false;
		for (var type : WarehouseResourceType.values()) {
			for (var entry : packet.warehouse.get(type).entrySet()) {
				if (null == context.getWarehouse().get(type).get(entry.getKey())) {
					context.getWarehouse().get(type).put(entry.getKey(), entry.getValue());
					needsBroadcast = true;
				}
			}
		}

		if (needsBroadcast) {
			packet.warehouse = context.getWarehouse().get(); // This is optional, i think :)
			context.getBroadcaster().send(packet);
		}


		return new NoOpPacket();
	}

}
