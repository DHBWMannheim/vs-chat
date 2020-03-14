package vs.chat.client;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Gui {
    //TODO: Use material-ui-swing from https://github.com/vincenzopalazzo/material-ui-swing
    private static JFrame rootPane() {
        JFrame fenster = new JFrame();
        fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenster.setMinimumSize(new Dimension(400, 600));
        fenster.setVisible(true);
        fenster.pack();
        return fenster;
    }

    private static JPanel footer() {
        //TODO: Replace Change Item to Emoji Icon and implement Action Listener for Emoji selection
        JPanel footerPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton emojiIcon = new JButton("Change Item!");
        JTextArea messageInput = new JTextArea("Nachricht", 1, 30);
        footerPannel.add(emojiIcon);
        footerPannel.add(messageInput);
        return footerPannel;
    }

    private static JPanel header(String username) {
        //TODO: Replace Change Button to User Immage
        JPanel headerPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton userIcon = new JButton("<--");
        JLabel chatName = new JLabel(username);
        JButton userImmage = new JButton("Change Button");
        headerPannel.add(userIcon);
        headerPannel.add(chatName);
        headerPannel.add(userImmage);
        return headerPannel;

    }

    private static JPanel displayMessage(String message) {
        //TODO: Differ between Incoming and Outgoing Message, Add Border
        JPanel messagePannel = new JPanel();
        JLabel content = new JLabel(message);
        messagePannel.add(content);
        return messagePannel;
    }

    private static JPanel messsageInput(String message) {
        JPanel inputPannel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPannel.add(nachricht);
        nachricht.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        return inputPannel;
    }

    public static void main(String[] args) {

        JFrame rootPane = rootPane();
        rootPane.add(header("Max Mustermann"), BorderLayout.NORTH);
        rootPane.add(displayMessage("Hallo"), BorderLayout.WEST);
        rootPane.add(displayMessage("Hi wie gehts?"), BorderLayout.EAST);
        rootPane.add(footer(), BorderLayout.SOUTH);
        rootPane.pack();
    }
}
