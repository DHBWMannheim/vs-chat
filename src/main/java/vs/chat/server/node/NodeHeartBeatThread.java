package vs.chat.server.node;

import java.io.IOException;
import java.io.ObjectOutputStream;

import vs.chat.packets.NoOpPacket;

public class NodeHeartBeatThread extends Thread {

	private boolean isCloseRequested = false;
	private final ObjectOutputStream out;
	private final static int BEAT_RATE = 1000;

	public NodeHeartBeatThread(final ObjectOutputStream out) {
		this.out = out;
	}

	@Override
	public void run() {
		while (!this.isCloseRequested) {
			try {
				out.writeObject(new NoOpPacket());
				out.flush();
				Thread.sleep(BEAT_RATE);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		this.isCloseRequested = true;
	}
}
