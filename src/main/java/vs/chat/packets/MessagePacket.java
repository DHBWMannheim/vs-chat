package vs.chat.packets;

public class MessagePacket implements Packet {

	public int target;
	public String content;

	@Override
	public String toString() {
		return "TO: " + target + "; CONTENT: " + content;
	}
}
