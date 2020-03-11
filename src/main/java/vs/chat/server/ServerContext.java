package vs.chat.server;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import vs.chat.packets.Packet;
import vs.chat.server.listener.Listener;

public class ServerContext {

//	private final Set<ConnectionHandler> connections = new TreeSet<>();// TODO synchronize
	private final List<Listener<? extends Packet, ? extends Packet>> listeners;
	private final BrotherNodeBroadcaster broadcaster;
	private boolean isCloseRequested = false;

	public ServerContext(final List<Listener<? extends Packet, ? extends Packet>> listeners, String tempBrotherAdress,
			int tempBrotherPort) {
		this.listeners = listeners;
		this.broadcaster = new BrotherNodeBroadcaster(tempBrotherAdress, tempBrotherPort, this);
	}

	public boolean isCloseRequested() {
		return isCloseRequested;
	}

	void setCloseRequested(boolean isCloseRequested) {
		this.isCloseRequested = isCloseRequested;
	}

	public List<Listener<? extends Packet, ? extends Packet>> getListeners() {
		return listeners;
	}

	public BrotherNodeBroadcaster getBroadcaster() {
		return broadcaster;
	}

}
