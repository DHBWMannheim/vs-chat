package vs.chat.client;

import java.io.*;
import java.net.Socket;

import vs.chat.client.CMD.Cmd;
import vs.chat.client.UI.ClientGUI;
// import vs.chat.client.UI.Gui;

public class Client {

	private static int PORT = 9876;

	public static void main(String[] args) {
		String hostname = "localhost";
		ObjectOutputStream objectOut;
		Socket socket;

		try {
			socket = new Socket(hostname, PORT);
			objectOut = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream networkIn = new ObjectInputStream(socket.getInputStream());

			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			String userInput;

			ClientApiImpl api = new ClientApiImpl(socket, objectOut, networkIn);

			do {
				System.out.print("Start with GUI [Y/n]? ");
				userInput = userIn.readLine();
			} while (!(userInput.toLowerCase().equals("y") || userInput.toLowerCase().equals("n")));

			if (userInput.toLowerCase().equals("y")) {
				new ClientGUI(api);
			} else if (userInput.toLowerCase().equals("n")) {
				new Cmd(api);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}