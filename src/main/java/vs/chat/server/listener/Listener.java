package vs.chat.server.listener;

import packets.Packet;

public interface Listener<T extends Packet, R extends Packet> {

	public R next(final T packets);
}
