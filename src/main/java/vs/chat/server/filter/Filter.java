package vs.chat.server.filter;

import vs.chat.packets.Packet;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;

public interface Filter {

	public default boolean canActivate(final Packet packet, final ServerContext context, final ConnectionHandler handler) {
		return true;
	}
	
	public default void postHandle(final Packet packet, final ServerContext context, final ConnectionHandler handler) {
		
	}
	
}
