package vs.chat.client.keyfile;

import java.math.BigInteger;
import java.util.UUID;

public class PrivateKeyEntity implements Comparable<PrivateKeyEntity>, Fileable {

	private static final long serialVersionUID = -3241255618869180195L;
	private final UUID chatId;
	private BigInteger privateKey;

	public PrivateKeyEntity(final UUID chatId, final BigInteger privateKey) {
		this.chatId = chatId;
		this.privateKey = privateKey;
	}

	public BigInteger getPrivateKey() {
		return this.privateKey;
	}

	public void setPrivateKey(BigInteger privateKey) {
		this.privateKey = privateKey;
	}

	@Override
	public boolean equals(Object obj) {
		return this.chatId.equals(obj);
	}

	@Override
	public UUID getId() {
		return this.chatId;
	}

	@Override
	public int compareTo(final PrivateKeyEntity p) {
		return this.chatId.compareTo(p.chatId);
	}
}
