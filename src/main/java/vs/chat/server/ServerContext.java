package vs.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import vs.chat.packets.Packet;
import vs.chat.server.filter.Filter;
import vs.chat.server.listener.Listener;
import vs.chat.server.node.NodeBroadcaster;
import vs.chat.server.node.NodeConfig;
import vs.chat.server.persistence.Persister;
import vs.chat.server.warehouse.Warehouse;

public class ServerContext {
	private final List<Listener<? extends Packet, ? extends Packet>> listeners;
	private final List<Filter> filters;

	private final NodeBroadcaster broadcaster;
	private final List<ConnectionHandler> connections = Collections.synchronizedList(new ArrayList<>());
	private final Warehouse warehouse;
	private final AtomicBoolean isCloseRequested = new AtomicBoolean(false);
	private final Persister persister = new Persister(this);

	public ServerContext(
			final String warehouseSaveFileIdentifier,
			final List<Listener<? extends Packet, ? extends Packet>> listeners,
			final List<Filter> filters,
			final NodeConfig... configs) {
		this.warehouse = new Warehouse(warehouseSaveFileIdentifier);
		this.listeners = listeners;
		this.filters = filters;
		this.broadcaster = new NodeBroadcaster(this, configs);
		this.persister.start();
	}

	public AtomicBoolean isCloseRequested() {
		return isCloseRequested;
	}

	void close() throws IOException, InterruptedException {
		this.isCloseRequested.set(true);
		System.out.println("awaiting close");
		for (var connection : this.connections) {
			connection.join();
		}
		for (var listener : this.listeners) {
			listener.close();
		}
		this.persister.join();
	}

	public List<Listener<? extends Packet, ? extends Packet>> getListeners() {
		return listeners;
	}
	
	public List<Filter> getFilters() {
		return filters;
	}

	public NodeBroadcaster getBroadcaster() {
		return broadcaster;
	}

	public List<ConnectionHandler> getConnections() {
		return connections;
	}

	public Optional<ConnectionHandler> getConnectionForUserId(final UUID id) {
		return this.connections.stream().filter(connection -> connection.getConnectedToUserId().isPresent()
				&& connection.getConnectedToUserId().get().equals(id)).findFirst();
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

}
