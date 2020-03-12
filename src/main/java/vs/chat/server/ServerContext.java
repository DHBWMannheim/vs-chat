package vs.chat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import vs.chat.packets.Packet;
import vs.chat.server.listener.Listener;

public class ServerContext {
	private final List<Listener<? extends Packet, ? extends Packet>> listeners;
	private final BrotherNodeBroadcaster broadcaster;
	private final List<ConnectionHandler> connections = Collections.synchronizedList(new ArrayList<>());
	private final Warehouse warehouse = new Warehouse();
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

	public List<ConnectionHandler> getConnections() {// TODO syncronize
		return connections;
	}

	public Optional<ConnectionHandler> getConnectionForUserId(final Integer id) {
		return this.connections.stream().filter(
				connection -> {
					System.out.println(connection.getConnectedToUserId());
					System.out.println(id);
					System.out.println(connection.getConnectedToUserId().equals(id));
					return connection.getConnectedToUserId() != null && connection.getConnectedToUserId().equals(id);
				})
				.findFirst();
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

}
