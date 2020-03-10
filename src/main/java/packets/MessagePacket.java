package packets;

import java.io.Serializable;

public class MessagePacket implements Serializable {

	int chatId;
	int clientId;
	int timestamp;
	String content;
}
