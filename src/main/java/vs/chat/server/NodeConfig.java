package vs.chat.server;

public class NodeConfig {

	private final String address;
	private final Integer port;

	public NodeConfig(String address, Integer port) {
		super();
		this.address = address;
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public Integer getPort() {
		return port;
	}

}
