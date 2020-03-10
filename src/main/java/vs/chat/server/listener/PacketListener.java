package vs.chat.server.listener;

import packets.Packet;

public interface PacketListener<T extends Packet, R extends Packet> {

	public R next(final T packets);
}
