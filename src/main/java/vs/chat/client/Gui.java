package vs.chat.client;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Gui {
    //TODO: Use material-ui-swing from https://github.com/vincenzopalazzo/material-ui-swing
    private static JFrame rootPane() {
        JFrame fenster = new JFrame();
        fenster.setLayout(new BorderLayout());
        fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenster.setMinimumSize(new Dimension(400, 600));
        fenster.setVisible(true);
        fenster.pack();
        return fenster;
    }

    private static JPanel footer() {
        //TODO: Implement Action Listener for Emoji selection
        JPanel footerPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon imageIcon = new ImageIcon("src/main/java/vs/chat/client/emoji.png");
        Image image = imageIcon.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        JLabel emoji = new JLabel(new ImageIcon(image));
        JTextArea messageInput = new JTextArea("Nachricht", 2, 30);
        footerPannel.add(emoji);
        footerPannel.add(messageInput);
        return footerPannel;
    }

    private static JPanel header(String username) {
        //TODO: Implement Action Listener to get back to Recent User view
        JPanel headerPannel = new JPanel(new GridLayout(0, 3));
        ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/profile.png");
        Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        JLabel profile = new JLabel(new ImageIcon(imageProfile));

        ImageIcon imageIconBack = new ImageIcon("src/main/java/vs/chat/client/back.png");
        Image imageBack = imageIconBack.getImage().getScaledInstance(60, 35, java.awt.Image.SCALE_SMOOTH);
        JLabel profileBack = new JLabel(new ImageIcon(imageBack));

        JLabel chatName = new JLabel(username);

        headerPannel.add(profileBack);
        headerPannel.add(chatName);
        headerPannel.add(profile);
        return headerPannel;

    }

    private static JPanel displayMessage(String message) {
        //TODO: Differ between Incoming and Outgoing Message, fix Position
        JPanel messagePannel = new JPanel(new FlowLayout());
        JLabel content = new JLabel(message);
        content.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        messagePannel.add(content);
        return messagePannel;
    }

    private static JPanel messsageInput(String message) {
        JPanel inputPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPannel.add(nachricht);
        return inputPannel;
    }

    private static JPanel displayRecentContacts() {
        //TODO: Create Border arround each contact
        JPanel contactsPannel = new JPanel(new GridLayout(0, 2));
        ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/profile.png");
        Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        JLabel profile = new JLabel(new ImageIcon(imageProfile));
        JLabel userName = new JLabel("Nutzername");
        contactsPannel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        contactsPannel.add(profile);
        contactsPannel.add(userName);

        return contactsPannel;
    }

    public static void main(String[] args) {

        JFrame rootPane = rootPane();

        rootPane.getContentPane().add(displayRecentContacts());

        rootPane.getContentPane().add(header("Max Mustermann"), BorderLayout.NORTH);
        rootPane.getContentPane().add(displayMessage("Hallo"), BorderLayout.WEST);
        rootPane.getContentPane().add(displayMessage("Hi wie gehts?"), BorderLayout.EAST);
        rootPane.getContentPane().add(footer(), BorderLayout.SOUTH);

        rootPane.pack();
    }
}
