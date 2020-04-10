package vs.chat.client.keyfile;

public enum KeyfileResourceType {
	PRIVATEKEY("PrivateKey");

	public final String label;

	private KeyfileResourceType(final String label) {
		this.label = label;
	}
}
