package vs.chat.packets;

import java.util.UUID;

public class MessagePacket extends Packet {

	public UUID target;
	public String content;

	@Override
	public String toString() {
		return "TO: " + target + "; CONTENT: " + content;
	}
}
