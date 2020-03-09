package vs.chat.server;

import java.io.Serializable;

public class Chat implements Serializable, IIdentifiable {

	private static final long serialVersionUID = -8485304096241606696L;
	private Integer id;
	private Integer[] clients;
	private Message[] messages;

	public Integer getId() {
		return id;
	}

	public Integer[] getClients() {
		return clients;
	}

	public Message[] getMessages() {
		return messages;
	}

}
