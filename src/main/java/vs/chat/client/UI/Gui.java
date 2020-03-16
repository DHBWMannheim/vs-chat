package vs.chat.client.UI;

import vs.chat.entities.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

public class Gui {


    //TODO: Use material-ui-swing from https://github.com/vincenzopalazzo/material-ui-swing
    private static JFrame rootPanel() {
        JFrame fenster = new JFrame();

        Image logo = Toolkit.getDefaultToolkit().getImage("src/main/java/vs/chat/client/UI/mario.png");
        fenster.setIconImage(logo);

        fenster.setLayout(new BorderLayout());
        fenster.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        fenster.setMinimumSize(new Dimension(400, 600));
        fenster.setVisible(true);
        fenster.setResizable(false);
        fenster.pack();
        return fenster;
    }

    private static JPanel footer() {
        //TODO: Implement Action Listener for Emoji selection
        //TODO: Overflowing TextArea (both directions), -> firm TextArea
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon imageIcon = new ImageIcon("src/main/java/vs/chat/client/UI/emoji.png");
        Image image = imageIcon.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        JLabel emoji = new JLabel(new ImageIcon(image));
        JTextArea messageInput = new JTextArea("Nachricht",2,  30);
        footerPanel.add(emoji);
        footerPanel.add(messageInput);
        footerPanel.setBorder(BorderFactory.createEtchedBorder());

        MouseListener emojiMouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Todo: Implement Emoji Selection Panel
                JPanel emojiSelection = new JPanel(new GridLayout(5,2));

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        emoji.addMouseListener(emojiMouseListener);



        return footerPanel;
    }

    private static JPanel header(String username) {
        //TODO: Implement Action Listener to get back to Recent User view
        JPanel headerPanel = new JPanel(new GridLayout(0, 3));
        ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/UI/profile.png");
        Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        JLabel profile = new JLabel(new ImageIcon(imageProfile));

        ImageIcon imageIconBack = new ImageIcon("src/main/java/vs/chat/client/UI/back.png");
        Image imageBack = imageIconBack.getImage().getScaledInstance(60, 35, java.awt.Image.SCALE_SMOOTH);
        JLabel profileBack = new JLabel(new ImageIcon(imageBack));

        JLabel chatName = new JLabel(username);

        headerPanel.add(profileBack);
        headerPanel.add(chatName);
        headerPanel.add(profile);
        headerPanel.setBorder(BorderFactory.createEtchedBorder());
        return headerPanel;

    }
    private static JScrollPane DisplayMessages(){
        //TODO: Implementation needed after Chat Entity is provided!
        return new JScrollPane();
    }

    private static JPanel displayNewMessage(Message message) {
        JPanel messagePanel = new JPanel(new GridLayout(2,0));
        JLabel userName = new JLabel(message.getOrigin().toString());
        JLabel content = new JLabel(message.getContent());
        messagePanel.add(userName);
        messagePanel.add(content);
        messagePanel.setBorder(BorderFactory.createEtchedBorder());

        if(message.getOrigin().equals(message.getId())) {
            messagePanel.setBackground(Color.GREEN);
        }else{
            messagePanel.setBackground(Color.GRAY);
        }

        messagePanel.setMaximumSize(new Dimension(350,30));

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        // boxPanel.setMaximumSize(new Dimension(350, 40));
        boxPanel.add(messagePanel);

        return boxPanel;
    }

    private static JPanel messageInput(String message) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPanel.add(nachricht);
        return inputPanel;
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

       JFrame rootPanel = rootPanel();
        String[] nutzernamen = {"Max Mustermann","Patrick Mischka", "Michael Angermeier", "Aaron Schweig","Troy Kessler","Matthias von End", "Jan Grübener"};
        Message firstMessage = new Message();
        firstMessage.setContent("Hello World!");
        firstMessage.setOrigin(UUID.fromString("16a9d592-79a7-4386-bbca-3a78fc091d34"));


        //rootPanel.getContentPane().add(displayRecentContacts(nutzernamen));

        rootPanel.getContentPane().add(header("Max Mustermann"), BorderLayout.NORTH);
        rootPanel.getContentPane().add(displayNewMessage(firstMessage));
        rootPanel.getContentPane().add(footer(), BorderLayout.SOUTH);



        rootPanel.pack();

    }

}
