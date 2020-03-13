package vs.chat.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import vs.chat.packets.Packet;
import vs.chat.server.listener.Listener;

public class ServerContext {
	private final List<Listener<? extends Packet, ? extends Packet>> listeners;
	private final NodeBroadcaster broadcaster;
	private final List<ConnectionHandler> connections = Collections.synchronizedList(new ArrayList<>());
	private final Warehouse warehouse = new Warehouse();
	private boolean isCloseRequested = false;

	public ServerContext(final List<Listener<? extends Packet, ? extends Packet>> listeners,
			final NodeConfig... configs) {
		this.listeners = listeners;
		this.broadcaster = new NodeBroadcaster(this, configs);
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

	public NodeBroadcaster getBroadcaster() {
		return broadcaster;
	}

	public List<ConnectionHandler> getConnections() {// TODO syncronize
		return connections;
	}

	public Optional<ConnectionHandler> getConnectionForUserId(final UUID id) {
		return this.connections.stream().filter(
				connection -> connection.getConnectedToUserId() != null && connection.getConnectedToUserId().equals(id))
				.findFirst();
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

}
