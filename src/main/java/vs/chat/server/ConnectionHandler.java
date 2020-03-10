package vs.chat.server;

import java.io.IOException;
import java.net.Socket;

import vs.chat.packets.Packet;

public class ConnectionHandler extends Thread{

	private final Socket client;
	private final ServerContext context;

	public ConnectionHandler(final Socket client, final ServerContext context) {
		this.client = client;
		this.context = context;
	}

	@Override
	public void run() {
//		while(!this.context.isCloseRequested()) {
//			System.out.println("Handling");
//		}
//		try {
//			this.client.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public void pushTo(final Packet packet) {
		//TODO pushes something to the client of this ConnectionHandler
		//TODO synconize this
	}

}
