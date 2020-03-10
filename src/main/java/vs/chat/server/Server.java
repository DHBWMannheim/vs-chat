package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import packets.Packet;
import vs.chat.server.listener.Listener;
import vs.chat.server.listener.PacketListener;

public class Server implements Runnable {

	private static final int PORT = 9876;
	// TODO connect nodes

	private final ServerContext context = new ServerContext();
	private final List<PacketListener<?,?>> listeners = new ArrayList<>();

	public Server() {

	}

	@Override
	public void run() {
		System.out.println("Starting Server on Port: " + PORT);
		try (var socket = new ServerSocket(PORT)) {

			createListener();

			while (!this.context.isCloseRequested()) {
				try {
					var clientSocket = socket.accept();
					var inputStream = new ObjectInputStream(clientSocket.getInputStream());
					var outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					List<Packet> receivedPackets = new ArrayList<>();
					Object object = null;
					System.out.println("Starting to receive");
					while ((object = inputStream.readObject()) != null) {
						System.out.println("Received: " + object.getClass().getSimpleName());
						receivedPackets.add((Packet) object);
					}
					//TODO filter packets
					System.out.println("continue");
					// Call all listeners (login listener, message listener, broadcaster)

					// TEMP
					clientSocket.close();
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Starting Server...");
	}

	private List<Listener> createListener() {
		try {
			Class<?>[] classes = Reflector.getClasses("vs.chat");
			for (Class<?> c : classes) {
				for (Annotation annotation : c.getDeclaredAnnotations()) {
					if (annotation instanceof Listener) {
						System.out.println("Creating a listener");
						try {
							var constructor = c.getConstructor();
							var listener = constructor.newInstance();
							System.out.println("created " + listener.getClass().getSimpleName());
							listeners.add((PacketListener<?,?>) listener);
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

		return List.of();
	}
}
