package vs.chat.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static int PORT = 9876;

    public static void main(String[] args) {
        String hostname = "localhost";
        PrintWriter networkOut;
        BufferedReader networkIn;

        try {
            Socket socket = new Socket(hostname, PORT);
            networkOut = new PrintWriter(socket.getOutputStream());
            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Open GUI

            JFrame mainFrame = new JFrame("DHBW Chat");
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(500, 700);

            // add elements
            JPanel mainPanel = new JPanel();

            JLabel usernameLabel = new JLabel("Username:");
            JLabel passwordLabel = new JLabel("Passwort:");

            JTextField username = new JTextField("", 32);
            JTextField password = new JPasswordField("", 32);

            JButton loginButton = new JButton("Login");

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        System.out.println(username.getText());
                        System.out.println(password.getText());
                        networkOut.println("LOGIN");
                        networkOut.println(username.getText() + ":" + password.getText());

                        networkOut.flush();

                        String token = networkIn.readLine();
                        System.out.println(token);

                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            });

            mainPanel.add(usernameLabel);
            mainPanel.add(username);

            mainPanel.add(passwordLabel);
            mainPanel.add(password);

            mainPanel.add(loginButton);

            mainFrame.add(mainPanel);

            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            mainFrame.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
