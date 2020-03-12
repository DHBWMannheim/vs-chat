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
		Socket socket = null;

		try {
//            while (true) {
			socket = new Socket(hostname, PORT);
//                networkOut = new PrintWriter(socket.getOutputStream());
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream networkIn = new ObjectInputStream(socket.getInputStream());
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
			l.username = "test" + Math.random();
			l.password = "password";
			objectOut.writeObject(l);
			objectOut.flush();

			System.out.println(networkIn.readObject());
//int i=0;

			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						var m = new MessagePacket();
						m.content = "Super Awesome Message with super long text " + Math.random();
						m.target = 2;
						try {
							objectOut.writeObject(m);
							objectOut.flush();
							Thread.sleep(10000);
						} catch (IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

//						i++;
					}
				}
			}).start();
			new Thread(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							System.out.println(networkIn.readObject());
						} catch (IOException | ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

//						i++;
					}
				}
			}).start();

//                networkOut.println("SEND");
//                networkOut.println("username:passwort");
//                networkOut.flush();
//            }

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
//			try {
//				if (socket != null) {
////					networkIn.close();
//
////					socket.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
		}
	}

}
