package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import packets.Packet;
import vs.chat.server.listener.Listener;

public class Server implements Runnable {

	private static final int PORT = 9876;
	// TODO connect nodes

	private final ServerContext context = new ServerContext();
	private final List<Listener<? extends Packet, ? extends Packet>> listeners = new ArrayList<>();

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
					var outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					var inputStream = new ObjectInputStream(clientSocket.getInputStream());
					List<Packet> receivedPackets = new ArrayList<>();
					Object object = null;
					System.out.println("Starting to receive");
					while ((object = inputStream.readObject()) != null) { //TODO check if connection still open -> EOF
						System.out.println("Received: " + object.getClass().getSimpleName());
						var packet = (Packet) object;
						receivedPackets.add(packet);

						for (var listener : listeners) {

							try {

								var methods = listener.getClass().getMethods();
								for(var method : methods) {
									
									if(method.getName().equals("next")) {//TODO lookup in interface
										var packetType = method.getParameters()[0];
										
										if(packet.getClass().equals(packetType.getType())) {
											System.out.println("Fitts");
											var retu = method.invoke(listener, packet);	//TODO clone Packet					
											System.out.println(retu);
											outputStream.writeObject(retu);
											outputStream.flush();
										}
									}
									
								}

							} catch (SecurityException  e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					// TODO filter packets
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

	private void createListener() {
		try {
			Class<?>[] classes = Reflector.getClasses("vs.chat");
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

	}
}
