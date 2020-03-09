package vs.chat.server;

import java.io.IOException;
import java.io.PrintWriter;

public class Worker implements Runnable {

	private final ServerContext context;

	public Worker(final ServerContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		while (!this.context.isCloseRequested()) {
			var socket = this.context.getQueue().poll();
			if(null != socket) {
				try {
					var outer = new PrintWriter(socket.getOutputStream());
					outer.write("TEST");
					outer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
