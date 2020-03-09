package vs.chat.server;

import java.net.Socket;

public class Worker implements Runnable {

	private final ServerContext context;
	private final Integer clientId;
	private final Socket socket;

	public Worker(final ServerContext context, final Integer clientId, final Socket socket) {
		this.context = context;
		this.clientId = clientId;
		this.socket = socket;
	}

	@Override
	public void run() {
		while (!this.context.isCloseRequested()) {
			
		}
	}

}
