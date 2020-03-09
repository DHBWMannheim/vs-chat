package vs.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Map;
import java.util.TreeMap;

public class Server implements Runnable {

	private static final int PORT = 9876;
	private static final Map<Integer, Worker> WORKERS = new TreeMap<>();

	public static void main(String[] args) {
		var server = new Server();
		

		var listener = new Thread(server);
		listener.start();
		try {
			listener.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private final ServerContext context = new ServerContext();
	

	public ServerContext getContext() {
		return context;
	}

	@Override
	public void run() {
		try (var socket = new ServerSocket(PORT)) {
			while (!this.context.isCloseRequested()) {
				try (var clientSocket = socket.accept()) {
					var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					var command = reader.readLine();
					if(Command.LOGIN.command.equals(command)) {
						System.out.println("login");
						System.out.println(reader.readLine());
						
					}
					
					reader.close();
				} catch (IOException e) {
					context.setCloseRequested(true);
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			context.setCloseRequested(true);
			e1.printStackTrace();
		}
	}
}
