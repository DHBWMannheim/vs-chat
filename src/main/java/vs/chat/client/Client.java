package vs.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import vs.chat.packets.LoginPacket;
import vs.chat.packets.MessagePacket;

public class Client {

	private static int PORT = 9876;

	public static void main(String[] args) throws InterruptedException {
		String hostname = "localhost";
		ObjectOutputStream objectOut;
		ObjectInputStream networkIn = null;
		Socket socket = null;

		try {
//            while (true) {
			socket = new Socket(hostname, PORT);
//                networkOut = new PrintWriter(socket.getOutputStream());
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			networkIn = new ObjectInputStream(socket.getInputStream());
//                BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
//                String userInput = userIn.readLine();
//                if (userInput.equals("/exit")) {
//                    System.out.println("Exiting...");
//                    break;
//                }
//                networkOut.println("LOGIN");
//                networkOut.println("woop:woopToo");
//                networkOut.flush();
//                String response = networkIn.readLine();
//                System.out.println(response);
//                
//                networkOut.println("SEND");
//                networkOut.println("woop:woopToo");
//                networkOut.flush();

			var l = new LoginPacket();
			l.username = "test";
			l.password = "password";
			objectOut.writeObject(l);
			objectOut.flush();

			System.out.println(networkIn.readObject());
int i=0;
			while(true) {
				var m = new MessagePacket();
				m.content = "Super Awesome Message with super long text "+i;
				m.target = 1;
				objectOut.writeObject(m);
				objectOut.flush();
				System.out.println(networkIn.readObject());
				Thread.sleep(10000);
				i++;
			}


//                networkOut.println("SEND");
//                networkOut.println("username:passwort");
//                networkOut.flush();
//            }

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					networkIn.close();
					
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
