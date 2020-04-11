package vs.chat.server.persistance;

import java.io.IOException;

import vs.chat.server.ServerContext;

public class Persister extends Thread {

	private static final int SAVE_INTERVAL = 10000;
	private final ServerContext contex;

	public Persister(final ServerContext context) {
		this.contex = context;
	}

	@Override
	public void run() {
		var warehouse = this.contex.getWarehouse();
		warehouse.load();
		while (!this.contex.isCloseRequested().get()) {
			System.out.println("Saving...");

			try {
				warehouse.save();
				System.out.println("Save completed :)");
				Thread.sleep(SAVE_INTERVAL);
			} catch (IOException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
