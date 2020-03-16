package vs.chat.client;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import vs.chat.packets.CreateChatPacket;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.MessagePacket;

public class Client {

	private static int PORT = 9876;

//    public static void main(String[] args) {
//        String hostname = "localhost";
//        PrintWriter networkOut;
//        BufferedReader networkIn;
//
//        try {
//            Socket socket = new Socket(hostname, PORT);
//            networkOut = new PrintWriter(socket.getOutputStream());
//            networkIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//            // Open GUI
//
//            JFrame mainFrame = new JFrame("DHBW Chat");
//            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            mainFrame.setSize(500, 700);
//
//            // add elements
//            JPanel mainPanel = new JPanel();
//
//            JLabel usernameLabel = new JLabel("Username:");
//            JLabel passwordLabel = new JLabel("Passwort:");
//
//            JTextField username = new JTextField("", 32);
//            JTextField password = new JPasswordField("", 32);
//
//            JButton loginButton = new JButton("Login");
//
//            loginButton.addActionListener(new ActionListener() {
//                @Override
//                public void actionPerformed(ActionEvent e) {
//                    try {
//                        System.out.println(username.getText());
//                        System.out.println(password.getText());
//                        networkOut.println("LOGIN");
//                        networkOut.println(username.getText() + ":" + password.getText());
//
//                        networkOut.flush();
//
//                        String token = networkIn.readLine();
//                        System.out.println(token);
//
//                    } catch (IOException io) {
//                        io.printStackTrace();
//                    }
//                }
//            });
//
//            mainPanel.add(usernameLabel);
//            mainPanel.add(username);
//
//            mainPanel.add(passwordLabel);
//            mainPanel.add(password);
//
//            mainPanel.add(loginButton);
//
//            mainFrame.add(mainPanel);
//
//            mainFrame.addWindowListener(new WindowAdapter() {
//                @Override
//                public void windowClosing(WindowEvent event) {
//                    try {
//                        socket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//
//            mainFrame.setVisible(true);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
			l.username = "test2";
			l.password = "password";
			objectOut.writeObject(l);
			objectOut.flush();

			System.out.println(networkIn.readObject());
//int i=0;

			new Thread(new Runnable() {

				@Override
				public void run() {
//					var mm = new CreateChatPacket(UUID.fromString("16a9d592-79a7-4386-bbca-3a78fc091d34"),
//							UUID.fromString("a97082b8-6b7c-48e5-9c5d-df3082c731f4"));
//					try {
//						objectOut.writeObject(mm);
//						objectOut.flush();
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
////					
					while (true) {
						var m = new MessagePacket();
						m.content = "Super Awesome Message with super long text " + Math.random();
						m.target = UUID.fromString("eae7ee33-4c52-43f6-acc8-9d1b2ad4f2bb");
						try {
							objectOut.writeObject(m);
							objectOut.flush();
							Thread.sleep(10000);
						} catch (IOException | InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
