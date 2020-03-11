package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import vs.chat.packets.Packet;

public class BrotherNodeConnection extends Thread {

	private final String hostname;
	private final int port;
	private final ServerContext context;

	private Socket currentSocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private final ConcurrentLinkedQueue<Packet> sendQueue = new ConcurrentLinkedQueue<>();

	public BrotherNodeConnection(final String hostname, final int port, final ServerContext context) {
		this.hostname = hostname;
		this.port = port;
		this.context = context;
	}

	@Override
	public void run() {
		try {
			this.reconnect();
			while (!this.context.isCloseRequested()) {
				try {
					if (this.currentSocket != null && out != null) {//TODO sleep this thread
						var packet = this.sendQueue.peek();
						if (packet != null) {
							out.writeObject(packet);
							out.flush();
							this.sendQueue.remove();
						}
					}
				} catch (IOException e) {
					this.reconnect();
				}
			}
			this.currentSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() throws IOException {// TODO syncornize to have out flushed
		if (null != this.currentSocket) {
			this.currentSocket.close();
		}
	}

	public void send(final Packet packet) {
		this.sendQueue.add(packet);
	}

	private void reconnect() {
		System.err.println("Attempting reconnect");
		try {
			this.close();
			this.currentSocket = new Socket(this.hostname, this.port);
			this.out = new ObjectOutputStream(this.currentSocket.getOutputStream());
			this.in = new ObjectInputStream(this.currentSocket.getInputStream());
			System.out.println("connected");
		} catch (IOException e) {
			e.printStackTrace();
			this.reconnect();
		}
	}
}
