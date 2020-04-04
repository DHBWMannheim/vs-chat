package vs.chat.packets;

import java.math.BigInteger;
import java.util.Set;
import java.util.UUID;

public class KeyEchangePacket implements Packet {

	public BigInteger content;
	public int requests;
	public UUID initiator;
	public Set<UUID> participants;

	public UUID target;
	public UUID origin;

	public BigInteger getContent() {
		return content;
	}

	public void setContent(BigInteger content) {
		this.content = content;
	}

	public int getRequests() {
		return requests;
	}

	public void setRequests(int requests) {
		this.requests = requests;
	}

	public UUID getInitiator() {
		return initiator;
	}

	public void setInitiator(UUID initiator) {
		this.initiator = initiator;
	}

	public Set<UUID> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<UUID> participants) {
		this.participants = participants;
	}

	public UUID getTarget() {
		return target;
	}

	public void setTarget(UUID target) {
		this.target = target;
	}

	public UUID getOrigin() {
		return origin;
	}

	public void setOrigin(UUID origin) {
		this.origin = origin;
	}

}
