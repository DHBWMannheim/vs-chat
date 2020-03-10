package vs.chat.server;

import java.util.Set;
import java.util.TreeSet;

public class ServerContext {

	private final Set<ConnectionHandler> connections = new TreeSet<>();//TODO syncronize
	private boolean isCloseRequested = false;

	public boolean isCloseRequested() {
		return isCloseRequested;
	}

	void setCloseRequested(boolean isCloseRequested) {
		this.isCloseRequested = isCloseRequested;
	}

}
