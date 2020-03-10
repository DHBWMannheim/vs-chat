package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

public class Server implements Runnable {

	private static final int PORT = 9876;
	//TODO connect nodes
	
	private final ServerContext context = new ServerContext();
	
	public Server() {
		
	}

	@Override
	public void run() {
		try (var socket = new ServerSocket(PORT)) {
			while (!this.context.isCloseRequested()) {
				try {
					var clientSocket = socket.accept();
					var inputStream = new ObjectInputStream(clientSocket.getInputStream());
					var outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
					//Call all listeners (login listener, message listener, broadcaster)
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting Server...");
	}
}
