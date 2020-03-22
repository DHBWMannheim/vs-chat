package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Optional;
import java.util.UUID;

import vs.chat.packets.Packet;

public class ConnectionHandler extends Thread {

	private final Socket client;
	private final ServerContext context;
	private final ObjectOutputStream outputStream;
	private final ObjectInputStream inputStream;

	private Optional<UUID> connectedToUserId = Optional.empty();

	public Optional<UUID> getConnectedToUserId() {
		return connectedToUserId;
	}

	public void setConnectedToUserId(UUID connectedToUserId) {
		this.connectedToUserId = Optional.of(connectedToUserId);
	}

	public ConnectionHandler(final Socket client, final ServerContext context, final ObjectOutputStream outputStream,
			final ObjectInputStream inputStream) {
		this.client = client;
		this.context = context;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("Connection Handler");
		while (!this.context.isCloseRequested().get()) {
			System.out.println("Handling");
			try {
				var object = inputStream.readObject();
				var packet = (Packet) object;
				this.handlePacket(packet);
			} catch (IOException | ClassNotFoundException e) {
				break;
			}
		}
		try {
			this.client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void handlePacket(final Packet packet) throws IOException {
		for (var listener : this.context.getListeners()) {
			try {
				var methods = listener.getClass().getMethods();
				for (var method : methods) {
					if (method.getName().equals("next")) {// TODO lookup in interface
						var packetType = method.getParameters()[0];
						if (packetType.getType().isAssignableFrom(packet.getClass())) {
							var retu = (Packet) method.invoke(listener, packet, this.context, this); // TODO clone
																										// Packet
							this.pushTo(retu);
						}
					}
				}

			} catch (SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	public void pushTo(final Packet packet) {
		// TODO synconize this
		try {
			if (null == packet)
				return;
			System.out.println("pushing" + packet);
			outputStream.writeObject(packet);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
			this.context.getConnections().remove(this);
			try {
				this.client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

}
