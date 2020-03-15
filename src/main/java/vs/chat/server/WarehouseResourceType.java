package vs.chat.server;

public enum WarehouseResourceType {
	USERS("Users"), CHATS("Chats"), MESSAGES("Messages");//TODO Merge Messages with Chats as Chats are only splitter

	public final String label;

	private WarehouseResourceType(final String label) {
		this.label = label;
	}
}
