package vs.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import packets.LoginPacket;

public class Client {

    private static int PORT = 9876;

    public static void main(String[] args) {
        String hostname = "localhost";
        PrintWriter networkOut;
        ObjectInputStream networkIn;
        Socket socket = null;

        try {
//            while (true) {
        	System.out.println("start");
                socket = new Socket(hostname, PORT);
                System.out.println("b");
//                networkOut = new PrintWriter(socket.getOutputStream());
                var objectOut = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("sett");
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
               
                
                System.out.println("start");
                	var l = new LoginPacket();
                	l.username = "test";
                	l.password = "password";
                	objectOut.writeObject(l);
                	System.out.println("b");
                objectOut.flush();
                
                System.out.println("reading");
                System.out.println(networkIn.readObject());
                System.out.println("read");
//                networkOut.println("SEND");
//                networkOut.println("username:passwort");
//                networkOut.flush();
//            }

        } catch (IOException | ClassNotFoundException e) {
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
