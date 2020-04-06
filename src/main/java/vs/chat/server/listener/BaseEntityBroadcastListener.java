package vs.chat.server.listener;

import java.io.IOException;

import vs.chat.packets.BaseEntityBroadcastPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public class BaseEntityBroadcastListener implements Listener<BaseEntityBroadcastPacket, NoOpPacket>{

	@Override
	public NoOpPacket next(BaseEntityBroadcastPacket packet, ServerContext context, ConnectionHandler handler)
			throws IOException {
		
		
		return null;
	}

}
