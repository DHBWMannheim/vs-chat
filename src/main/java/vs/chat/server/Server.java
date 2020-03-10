package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

import packets.Packet;

public class Server implements Runnable {

	private static final int PORT = 9876;
	// TODO connect nodes

	private final ServerContext context = new ServerContext();

	public Server() {

	}

	@Override
	public void run() {
		System.out.println("Starting Server on Port: " + PORT);
		try (var socket = new ServerSocket(PORT)) {
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
}
