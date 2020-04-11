package vs.chat.packets;

import java.util.UUID;

public class MessagePacket extends Packet {

	private static final long serialVersionUID = 555856012554738520L;
	private UUID target;
	private String content;

	public MessagePacket(final UUID target, final String content) {
		this.target = target;
		this.content = content;
	}

	@Override
	public String toString() {
		return "TO: " + target + "; CONTENT: " + content;
	}

	public UUID getTarget() {
		return target;
	}

	public String getContent() {
		return content;
	}
}
