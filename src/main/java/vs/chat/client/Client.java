package vs.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread {

    public static int PORT = 9876;

    @Override
    public void run() {
        String hostname = "localhost";
        PrintWriter networkOut;
        BufferedReader networkIn;
        Socket socket = null;

        try {
            while (true) {
                socket = new Socket(hostname, PORT);
                networkOut = new PrintWriter(socket.getOutputStream());
                networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));

                String userInput = userIn.readLine();
                if (userInput.equals("q")) {
                    break;
                }
                networkOut.println(userInput);
                networkOut.flush();

                String response = networkIn.readLine();
                System.out.println(response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
