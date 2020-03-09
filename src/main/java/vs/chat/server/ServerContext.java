package vs.chat.server;

public class ServerContext {

	private boolean isCloseRequested = false;
	public boolean isCloseRequested() {
		return isCloseRequested;
	}

	void setCloseRequested(boolean isCloseRequested) {
		this.isCloseRequested = isCloseRequested;
	}

}
