package vs.chat.server;

public class Worker implements Runnable {

	private final ServerContext context;

	public Worker(final ServerContext context) {
		this.context = context;
	}

	@Override
	public void run() {
		while (!this.context.isCloseRequested()) {
			System.out.println("Trying");

		}
	}

}
