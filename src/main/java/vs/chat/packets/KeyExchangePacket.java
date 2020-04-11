package vs.chat.packets;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public class KeyExchangePacket extends Packet {

	public BigInteger content;
	public int requests;
	public UUID initiator;
	public List<UUID> participants;
	public String chatName;

	public UUID target;
	public UUID origin;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((initiator == null) ? 0 : initiator.hashCode());
		result = prime * result + ((origin == null) ? 0 : origin.hashCode());
		result = prime * result + ((participants == null) ? 0 : participants.hashCode());
		result = prime * result + requests;
		result = prime * result + ((target == null) ? 0 : target.hashCode());
		return result;
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
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (initiator == null) {
			if (other.initiator != null)
				return false;
		} else if (!initiator.equals(other.initiator))
			return false;
		if (origin == null) {
			if (other.origin != null)
				return false;
		} else if (!origin.equals(other.origin))
			return false;
		if (participants == null) {
			if (other.participants != null)
				return false;
		} else if (!participants.equals(other.participants))
			return false;
		if (requests != other.requests)
			return false;
		if (target == null) {
			if (other.target != null)
				return false;
		} else if (!target.equals(other.target))
			return false;
		return true;
	}

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

	public List<UUID> getParticipants() {
		return participants;
	}

	public void setParticipants(List<UUID> participants) {
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

	public String getChatName() {
		return chatName;
	}

	public void setChatName(String chatName) {
		this.chatName = chatName;
	}

}
