package vs.chat.server.node;

import vs.chat.packets.NoOpPacket;

public class NodeHeartBeatThread extends Thread {

	private boolean isCloseRequested = false;
	private final NodeConnection out;
	private final static int BEAT_RATE = 1000;

	public NodeHeartBeatThread(final NodeConnection nodeConnection) {
		this.out = nodeConnection;
	}

	@Override
	public void run() {
		while (!this.isCloseRequested) {
			try {
				out.send(new NoOpPacket());
				Thread.sleep(BEAT_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		this.isCloseRequested = true;
	}
}
