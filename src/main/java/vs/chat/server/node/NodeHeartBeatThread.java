package vs.chat.server.node;

import vs.chat.packets.NoOpPacket;
import vs.chat.packets.NodeSyncPacket;

public class NodeHeartBeatThread extends Thread {

	private boolean isCloseRequested = false;
	private final NodeConnection out;
	private final static int BEAT_RATE = 1000;

	public NodeHeartBeatThread(final NodeConnection nodeConnection) {
		this.out = nodeConnection;
	}

	@Override
	public void run() {
		boolean connected = true;
		while (!this.isCloseRequested) {
			try {
				out.send(new NoOpPacket());
				if(!connected) {
					System.out.println("lets go");
					var nodeSyncPacket = new NodeSyncPacket();
					nodeSyncPacket.warehouse = out.getContext().getWarehouse().get();
					out.send(nodeSyncPacket);
				}
				connected = true;
				Thread.sleep(BEAT_RATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
				connected = false;
			}
		}
	}

	public void close() {
		this.isCloseRequested = true;
	}
}
