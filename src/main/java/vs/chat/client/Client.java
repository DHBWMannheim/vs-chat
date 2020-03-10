package vs.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import packets.LoginPacket;

public class Client {

    private static int PORT = 9876;

    public static void main(String[] args) {
        String hostname = "localhost";
        PrintWriter networkOut;
        BufferedReader networkIn;
        Socket socket = null;

        try {
//            while (true) {
                socket = new Socket(hostname, PORT);
//                networkOut = new PrintWriter(socket.getOutputStream());
                networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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
                var objectOut = new ObjectOutputStream(socket.getOutputStream());
                	var l = new LoginPacket();
                	l.username = "test";
                	l.password = "password";
                	objectOut.writeObject(l);
                objectOut.flush();
                
                String response = networkIn.readLine();
                System.out.println(response);
                
//                networkOut.println("SEND");
//                networkOut.println("username:passwort");
//                networkOut.flush();
//            }

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
