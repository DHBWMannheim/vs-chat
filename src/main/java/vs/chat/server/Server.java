package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import vs.chat.packets.Packet;
import vs.chat.server.filter.Filter;
import vs.chat.server.filter.PacketIdFilter;
import vs.chat.server.listener.BaseEntityBroadcastListener;
import vs.chat.server.listener.CreateChatListener;
import vs.chat.server.listener.GetMessagesListener;
import vs.chat.server.listener.KeyExchangeListener;
import vs.chat.server.listener.Listener;
import vs.chat.server.listener.LoginListener;
import vs.chat.server.listener.MessageListener;
import vs.chat.server.listener.NodeSyncListener;
import vs.chat.server.node.NodeConfig;

public class Server implements Runnable {

	private final int PORT;

	private final ServerContext context;

	public Server(final Integer port, final NodeConfig... configs) {
		this.PORT = port;
		var listeners = createListener();
		var filters = createFilters();
		this.context = new ServerContext(port.toString(), listeners, filters, configs);
	}

	@Override
	public void run() {
		System.out.println("Starting Server on Port: " + PORT);
		Thread.currentThread().setName("Server");

		try (var socket = new ServerSocket(PORT)) {
			while (!this.context.isCloseRequested().get()) {
				try {
					var clientSocket = socket.accept();
					var outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					var inputStream = new ObjectInputStream(clientSocket.getInputStream());

					var connectionHandler = new ConnectionHandler(clientSocket, this.context, outputStream,
							inputStream);
					this.context.getConnections().add(connectionHandler);
					connectionHandler.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Stopping Server...");
			this.context.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

	private List<Filter> createFilters() {
		var filters = new ArrayList<Filter>();
		filters.add(new PacketIdFilter());
		return filters;
	}

	private List<Listener<? extends Packet, ? extends Packet>> createListener() {
		var listeners = new ArrayList<Listener<? extends Packet, ? extends Packet>>();
		listeners.add(new CreateChatListener());
		listeners.add(new GetMessagesListener());
		listeners.add(new LoginListener());
		listeners.add(new MessageListener());
		listeners.add(new NodeSyncListener());
		listeners.add(new KeyExchangeListener());
		listeners.add(new BaseEntityBroadcastListener());
		return listeners;
	}
}
