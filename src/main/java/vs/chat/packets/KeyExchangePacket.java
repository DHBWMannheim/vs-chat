package vs.chat.packets;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class KeyExchangePacket extends Packet {

	private static final long serialVersionUID = -5028251640865142447L;
	private final BigInteger content;
	private final int requests;
	private final UUID initiator;
	private final List<UUID> participants;
	private final String chatName;

	private final UUID target;
	private UUID origin;

	public KeyExchangePacket(BigInteger content, int requests, UUID initiator, List<UUID> participants, String chatName,
			UUID target) {
		this.content = content;
		this.requests = requests;
		this.initiator = initiator;
		this.participants = participants;
		this.chatName = chatName;
		this.target = target;
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatName, content, initiator, origin, participants, requests, target);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KeyExchangePacket other = (KeyExchangePacket) obj;
		return Objects.equals(chatName, other.chatName) && Objects.equals(content, other.content)
				&& Objects.equals(initiator, other.initiator) && Objects.equals(origin, other.origin)
				&& Objects.equals(participants, other.participants) && requests == other.requests
				&& Objects.equals(target, other.target);
	}

	public BigInteger getContent() {
		return content;
	}

	public int getRequests() {
		return requests;
	}

	public UUID getInitiator() {
		return initiator;
	}

	public List<UUID> getParticipants() {
		return participants;
	}

	public UUID getTarget() {
		return target;
	}

	public UUID getOrigin() {
		return origin;
	}

	public void setOrigin(UUID origin) {
		this.origin = origin;
	}

	public String getChatName() {
		return chatName;
	}

}
