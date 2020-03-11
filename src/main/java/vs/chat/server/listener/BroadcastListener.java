package vs.chat.server.listener;

import vs.chat.packets.BroadcastPacket;
import vs.chat.packets.Packet;
import vs.chat.server.ServerContext;

public class BroadcastListener implements Listener<BroadcastPacket, Packet>{

	@Override
	public Packet next(final BroadcastPacket packet, final ServerContext context) {
		System.out.println("received broadcast " + packet.packet);
		return null;
	}

}
