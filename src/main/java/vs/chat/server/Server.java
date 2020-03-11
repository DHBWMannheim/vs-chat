package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import vs.chat.packets.Packet;
import vs.chat.server.listener.Listener;

public class Server implements Runnable {

	private final int PORT;
	
	private final ServerContext context;

	private final List<ConnectionHandler> connections = new ArrayList<>();

	public Server(int port, int brotherport) {
		this.PORT = port;
		var listeners = createListener();
		this.context = new ServerContext(listeners, "localhost", brotherport);
	}

	@Override
	public void run() {
		System.out.println("Starting Server on Port: " + PORT);
		try (var socket = new ServerSocket(PORT)) {
			while (!this.context.isCloseRequested()) {
				try {
					var clientSocket = socket.accept();
					var outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					var inputStream = new ObjectInputStream(clientSocket.getInputStream());

					var connectionHandler = new ConnectionHandler(clientSocket, this.context, outputStream,
							inputStream);
					this.connections.add(connectionHandler);
					connectionHandler.start();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			for (var connection : connections) {
				connection.join();
			}
			for (var listener : this.context.getListeners()) {
				listener.close();
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Starting Server...");
	}

	private List<Listener<? extends Packet, ? extends Packet>> createListener() {
		List<Listener<? extends Packet, ? extends Packet>> listeners = new ArrayList<>();
		try {
			Class<?>[] classes = Reflector.getClasses("vs.chat.server");
			for (Class<?> c : classes) {
				for (Class<?> cc : c.getInterfaces()) {
					if (Listener.class.equals(cc)) {
						System.out.println("creating listener of type: " + c.getSimpleName());
						try {
							var constructor = c.getConstructor();
							var listener = constructor.newInstance();
							listeners.add((Listener<?, ?>) listener);
						} catch (NoSuchMethodException | SecurityException | InstantiationException
								| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listeners;
	}
}
