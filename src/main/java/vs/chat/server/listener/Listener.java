package vs.chat.server.listener;

import java.io.ObjectOutputStream;
import java.util.List;

import packets.Packet;

public interface Listener {

	public void next(final List<Packet> in, final ObjectOutputStream out);
}
