package vs.chat.client.UI;

import vs.chat.entities.Message;

import javax.sql.rowset.FilteredRowSet;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.UUID;

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
        ImageIcon imageIcon = new ImageIcon("src/main/java/vs/chat/client/UI/emoji.png");
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
        ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/UI/profile.png");
        Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        JLabel profile = new JLabel(new ImageIcon(imageProfile));

        ImageIcon imageIconBack = new ImageIcon("src/main/java/vs/chat/client/UI/back.png");
        Image imageBack = imageIconBack.getImage().getScaledInstance(60, 35, java.awt.Image.SCALE_SMOOTH);
        JLabel profileBack = new JLabel(new ImageIcon(imageBack));

        JLabel chatName = new JLabel(username);

        headerPannel.add(profileBack);
        headerPannel.add(chatName);
        headerPannel.add(profile);
        return headerPannel;

    }
    private static JScrollPane DisplayMessages(){
        //TODO: Implementation needed after Chat Entity is provided!
        return new JScrollPane();
    }

    private static JPanel displayNewMessage(Message message) {
        //TODO: Differ between Incoming and Outgoing Message, fix Position
        JPanel messagePannel = new JPanel(new GridLayout(2,0));
        JLabel userName = new JLabel(message.getOrigin().toString());
        JLabel content = new JLabel(message.getContent());
        messagePannel.add(userName);
        messagePannel.add(content);
        messagePannel.setBorder(BorderFactory.createEtchedBorder());

        return messagePannel;
    }

    private static JPanel messsageInput(String message) {
        JPanel inputPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPannel.add(nachricht);
        return inputPannel;
    }

    private static JPanel displayRecentContacts(String[] userNames) {
        //TODO: Add Event Listener
        JPanel contactsPanel = new JPanel(new GridLayout(0, 1));
        for (String userName : userNames) {
            ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/UI/profile.png");
            Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            JLabel profile = new JLabel(new ImageIcon(imageProfile));
            JLabel userNameLabel = new JLabel(userName);
            JPanel userPanel = new JPanel(new GridLayout(1,2));
            userPanel.add(profile);
            userPanel.add(userNameLabel);
            userPanel.setBorder(BorderFactory.createEtchedBorder());
            contactsPanel.add(userPanel);
        }

        return contactsPanel;
    }

    public static void main(String[] args) {

        JFrame rootPane = rootPane();
        String[] nutzernamen = {"Max Mustermann","Patrick Mischka", "Michael Angermeier", "Aaron Schweig","Troy Kessler","Matthias von End", "Jan Grübener"};
        Message firstMessage = new Message();
        firstMessage.setContent("Hello World!");
        firstMessage.setOrigin(UUID.randomUUID());

        rootPane.getContentPane().add(displayRecentContacts(nutzernamen));

        /*rootPane.getContentPane().add(header("Max Mustermann"), BorderLayout.NORTH);
        rootPane.getContentPane().add(displayNewMessage(firstMessage));
        rootPane.getContentPane().add(footer(), BorderLayout.SOUTH);

         */

        rootPane.pack();

    }

}
