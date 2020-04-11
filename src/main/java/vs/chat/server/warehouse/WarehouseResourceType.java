package vs.chat.server.warehouse;

public enum WarehouseResourceType {
	USERS("Users"), CHATS("Chats"), MESSAGES("Messages");

	public final String label;

	private WarehouseResourceType(final String label) {
		this.label = label;
	}
}
