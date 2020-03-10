package vs.chat.server.deprecated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import vs.chat.server.ServerContext;
import vs.chat.server.persistance.PersistanceHandler;

public class Server implements Runnable {

	private static final int PORT = 9876;

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
	private final Map<Integer, Worker> CLIENTS = new TreeMap<>();
	private final Map<String, Client> knownUser = new TreeMap<>();

	public Server() {
		var tempClient = new Client(1, "woop", "woopToo");
		knownUser.put(tempClient.getUsername(), tempClient);

		var a = new PersistanceHandler<Client>();
		knownUser.values().stream().forEach(u -> {
			try {
				a.store(u);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		try {
			System.out.println(a.load(1));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ServerContext getContext() {
		return context;
	}

	@Override
	public void run() {
		try (var socket = new ServerSocket(PORT)) {
			while (!this.context.isCloseRequested()) {
				try {
					var clientSocket = socket.accept();
					var reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
					var command = reader.readLine();
					if (Command.LOGIN.command.equals(command)) {
						var creds = reader.readLine().split(":");
						var authClient = this.knownUser.get(creds[0]);
						if (authClient == null || !authClient.getPassword().equals(creds[1])) {
							// TODO Not ok
							System.out.println("NOT OK");
						} else {
							// OK
							System.out.println("OK");
							if (!this.CLIENTS.containsKey(authClient.getId())) {
								var worker = new Worker(context, authClient.getId(), clientSocket);
								this.CLIENTS.put(authClient.getId(), worker);
								new Thread(worker).start();
							}
						}
					}

				} catch (IOException e) {
					context.setCloseRequested(true);
					e.printStackTrace();
				}
			}
		} catch (

		IOException e1) {
			context.setCloseRequested(true);
			e1.printStackTrace();
		}
	}
}
