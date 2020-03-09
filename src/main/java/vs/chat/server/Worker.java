package vs.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker implements Runnable {

	private final ServerContext context;
	private final Integer clientId;
	private final Socket socket;

	public Worker(final ServerContext context, final Integer clientId, final Socket socket) {
		this.context = context;
		this.clientId = clientId;
		this.socket = socket;
		System.out.println("Created new Worker for clientId: " + clientId);
	}

	@Override
	public void run() {
		try {
			var reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			var writer = new PrintWriter(this.socket.getOutputStream());
			writer.println("connected");
			writer.flush();
			while (!this.context.isCloseRequested()) {//TODO Split with Every Command?
				System.out.println("Running");
				var line = "";
				while ((line = reader.readLine()) != null) {
					System.out.println("Read");
					System.out.println(line);
				}
			}
			this.socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
