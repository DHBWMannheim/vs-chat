package vs.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import vs.chat.packets.Packet;

public class ConnectionHandler extends Thread {

	private final Socket client;
	private final ServerContext context;
	private final ObjectOutputStream outputStream;
	private final ObjectInputStream inputStream;

	public ConnectionHandler(final Socket client, final ServerContext context, final ObjectOutputStream outputStream,
			final ObjectInputStream inputStream) {
		this.client = client;
		this.context = context;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		while (!this.context.isCloseRequested()) {
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
	
	private void handlePacket(Packet packet) throws IOException {
		for (var listener : this.context.getListeners()) {
			try {
				var methods = listener.getClass().getMethods();
				for (var method : methods) {
					if (method.getName().equals("next")) {// TODO lookup in interface
						var packetType = method.getParameters()[0];
						if (packet.getClass().equals(packetType.getType())) {
							var retu = method.invoke(listener, packet); // TODO clone Packet
							outputStream.writeObject(retu);
							outputStream.flush();
						}
					}
				}

			} catch (SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void pushTo(final Packet packet) {
		// TODO pushes something to the client of this ConnectionHandler
		// TODO synconize this
	}

}
