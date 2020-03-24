package vs.chat.server.node;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

import vs.chat.packets.NodeSyncPacket;
import vs.chat.packets.Packet;
import vs.chat.server.ServerContext;

public class NodeConnection extends Thread {

	private final String hostname;
	private final int port;
	private final ServerContext context;

	private Socket currentSocket;
	private ObjectOutputStream out;
//	private ObjectInputStream in;

	private final ConcurrentLinkedQueue<Packet> sendQueue = new ConcurrentLinkedQueue<>();
	private final Semaphore runSemaphore = new Semaphore(0);
	private NodeHeartBeatThread bodyGuard;

	public NodeConnection(final String hostname, final int port, final ServerContext context) {
		this.hostname = hostname;
		this.port = port;
		this.context = context;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Node Connection");
		try {
			this.reconnect();
			while (!this.context.isCloseRequested().get()) {
				runSemaphore.acquire();
				try {
					if (this.currentSocket != null && out != null) {
						var packet = this.sendQueue.peek();
						if (packet != null) {
							out.writeObject(packet);
							out.flush();
							this.sendQueue.remove();
						}
					}
				} catch (IOException e) {
					this.reconnect();
					this.runSemaphore.release();
				}
			}
			this.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	void close() throws IOException {// TODO syncornize to have out flushed
		if (null != this.bodyGuard)
			this.bodyGuard.close();
		if (null != this.currentSocket)
			this.currentSocket.close();
	}

	public void send(final Packet packet) {
		this.sendQueue.add(packet);
		this.runSemaphore.release();
	}

	private void reconnect() {
		System.err.println("Attempting reconnect");
		try {
			this.close();
			this.currentSocket = new Socket(this.hostname, this.port);
			this.out = new ObjectOutputStream(this.currentSocket.getOutputStream());
			this.bodyGuard = new NodeHeartBeatThread(this);
			this.bodyGuard.start();

			var nodeSyncPacket = new NodeSyncPacket();
			nodeSyncPacket.warehouse = this.context.getWarehouse().get();
			this.send(nodeSyncPacket);

//			this.in = new ObjectInputStream(this.currentSocket.getInputStream());
			System.out.println("connected");
		} catch (IOException e) {
			e.printStackTrace();
			this.reconnect();//TODO replace this with while
		}
	}

	public ServerContext getContext() {
		return context;
	}
	
	
}
