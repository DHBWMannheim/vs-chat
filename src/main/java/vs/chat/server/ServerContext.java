package vs.chat.server;

import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ServerContext {

	private boolean isCloseRequested = false;
	private final ConcurrentLinkedQueue<Socket> QUEUE = new ConcurrentLinkedQueue<>();

	public boolean isCloseRequested() {
		return isCloseRequested;
	}

	public ConcurrentLinkedQueue<Socket> getQueue() {
		return QUEUE;
	}

	void setCloseRequested(boolean isCloseRequested) {
		this.isCloseRequested = isCloseRequested;
	}

}
