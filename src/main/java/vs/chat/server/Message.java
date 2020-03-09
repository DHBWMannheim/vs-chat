package vs.chat.server;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = -6508790562250087309L;
	private int chatId;
	private int clientId;
	private int timestamp;
	private String content;
}
