package vs.chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;

public class Server implements Runnable {

	private static final int PORT = 9876;
	private static final int WORKER_COUNT = 4;
	private static final Thread[] WORKERS = new Thread[WORKER_COUNT];

	public static void main(String[] args) {
		var server = new Server();
		

		for (int i = 0; i < WORKER_COUNT; i++) {
			WORKERS[i] = new Thread(new Worker(server.getContext()));
			WORKERS[i].start();
		}

		var listener = new Thread(server);
		listener.start();
		try {
			for (int i = 0; i < WORKER_COUNT; i++) {
				WORKERS[i].join();
			}
			listener.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private final ServerContext context = new ServerContext();
	

	public ServerContext getContext() {
		return context;
	}


	public void run() {
		try (var socket = new ServerSocket(PORT)) {
			while (!this.context.isCloseRequested()) {
				try (var clientSocket = socket.accept()) {
					this.context.getQueue().offer(clientSocket);
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
