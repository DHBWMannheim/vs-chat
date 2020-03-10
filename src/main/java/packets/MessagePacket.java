package packets;

import java.io.Serializable;

public class MessagePacket implements Serializable, Packet {

	public int chatId;
	public int clientId;
	public int timestamp;
	public String content;
}
