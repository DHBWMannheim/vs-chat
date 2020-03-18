package vs.chat.client.UI;

import vs.chat.entities.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.UUID;

public class Gui {

    static JFrame rootPanel;
    static String[] nutzernamen = {"Max Mustermann", "Patrick Mischka", "Michael Angermeier", "Aaron Schweig", "Troy Kessler", "Matthias von End", "Jan Grübener"};

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
        //TODO: Overflowing TextArea (both directions), -> firm TextArea
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ImageIcon imageIcon = new ImageIcon("src/main/java/vs/chat/client/UI/emoji.png");
        Image image = imageIcon.getImage().getScaledInstance(30, 30, java.awt.Image.SCALE_SMOOTH);
        JLabel emoji = new JLabel(new ImageIcon(image));
        JTextArea messageInput = new JTextArea("Nachricht", 2, 30);
        footerPanel.add(emoji);
        footerPanel.add(messageInput);
        footerPanel.setBorder(BorderFactory.createEtchedBorder());

        MouseListener emojiMouseListener = new MouseListener() {
            int counter = 0;
            JPanel emojiPanel;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (counter % 2 == 0) {
                    emojiPanel = renderEmojiPanel();
                    emojiPanel.setVisible(true);
                    rootPanel.getContentPane().add(emojiPanel, BorderLayout.CENTER);
                    rootPanel.pack();
                    counter++;
                } else {
                    emojiPanel.setVisible(false);
                    rootPanel.getContentPane().remove(emojiPanel);
                    emojiPanel = null;
                    counter++;
                }
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

    private static JPanel renderEmojiPanel() {
        float fontsize = 32f;
        JPanel emojiSelection = new JPanel(new GridLayout(2, 5));
        JLabel smile = new JLabel("\t\uD83D\uDE04");
        smile.setHorizontalAlignment(JLabel.CENTER);
        smile.setFont(smile.getFont().deriveFont(fontsize));
        emojiSelection.add(smile);
        JLabel tears = new JLabel("\uD83D\uDE02");
        tears.setHorizontalAlignment(JLabel.CENTER);
        tears.setFont(tears.getFont().deriveFont(fontsize));
        emojiSelection.add(tears);
        JLabel upsideDown = new JLabel("\uD83D\uDE43");
        upsideDown.setHorizontalAlignment(JLabel.CENTER);
        upsideDown.setFont(upsideDown.getFont().deriveFont(fontsize));
        emojiSelection.add(upsideDown);
        JLabel winking = new JLabel("\uD83D\uDE09");
        winking.setHorizontalAlignment(JLabel.CENTER);
        winking.setFont(winking.getFont().deriveFont(fontsize));
        emojiSelection.add(winking);
        JLabel angel = new JLabel("\uD83D\uDE07");
        angel.setHorizontalAlignment(JLabel.CENTER);
        angel.setFont(angel.getFont().deriveFont(fontsize));
        emojiSelection.add(angel);
        JLabel kiss = new JLabel("\uD83D\uDE18");
        kiss.setHorizontalAlignment(JLabel.CENTER);
        kiss.setFont(kiss.getFont().deriveFont(fontsize));
        emojiSelection.add(kiss);
        JLabel savoring = new JLabel("\uD83D\uDE0B");
        savoring.setHorizontalAlignment(JLabel.CENTER);
        savoring.setFont(savoring.getFont().deriveFont(fontsize));
        emojiSelection.add(savoring);
        JLabel thinking = new JLabel("\uD83E\uDD14");
        thinking.setHorizontalAlignment(JLabel.CENTER);
        thinking.setFont(thinking.getFont().deriveFont(fontsize));
        emojiSelection.add(thinking);
        JLabel pedoo = new JLabel("\uD83D\uDE0F");
        pedoo.setHorizontalAlignment(JLabel.CENTER);
        pedoo.setFont(pedoo.getFont().deriveFont(fontsize));
        emojiSelection.add(pedoo);
        JLabel corona = new JLabel("\uD83D\uDE37");
        corona.setHorizontalAlignment(JLabel.CENTER);
        corona.setFont(corona.getFont().deriveFont(fontsize));
        emojiSelection.add(corona);

        //TODO: setMaximumSize isn´t workinig
        emojiSelection.setMaximumSize(new Dimension(400,100));
        JPanel centerEmojiPanel = new JPanel();
        centerEmojiPanel.setLayout(new BoxLayout(centerEmojiPanel, BoxLayout.Y_AXIS));
        centerEmojiPanel.add(emojiSelection);

        return centerEmojiPanel;
    }

    private static JPanel header(String username) {
        JPanel headerPanel = new JPanel(new GridLayout(0, 3));
        ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/UI/profile.png");
        Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
        JLabel profile = new JLabel(new ImageIcon(imageProfile));

        ImageIcon imageIconBack = new ImageIcon("src/main/java/vs/chat/client/UI/back.png");
        Image imageBack = imageIconBack.getImage().getScaledInstance(60, 35, java.awt.Image.SCALE_SMOOTH);
        JLabel profileBack = new JLabel(new ImageIcon(imageBack));

        JLabel chatName = new JLabel(username);


        MouseListener backMouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                JPanel recentConversations = displayRecentConversations(nutzernamen);
                recentConversations.setVisible(true);
                rootPanel.getContentPane().removeAll();
                rootPanel.getContentPane().add(recentConversations, BorderLayout.CENTER);
                rootPanel.pack();
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        };
        profileBack.addMouseListener(backMouseListener);
        headerPanel.add(profileBack);
        headerPanel.add(chatName);
        headerPanel.add(profile);
        headerPanel.setBorder(BorderFactory.createEtchedBorder());
        return headerPanel;

    }

    private static JScrollPane displayMessages() {
        //TODO: Implementation needed after Chat Entity is provided!
        return new JScrollPane();
    }

    private static JPanel displayNewMessage(Message message) {
        JPanel messagePanel = new JPanel(new GridLayout(2, 0));
        JLabel userName = new JLabel(message.getOrigin().toString());
        JLabel content = new JLabel(message.getContent());
        messagePanel.add(userName);
        messagePanel.add(content);
        messagePanel.setBorder(BorderFactory.createEtchedBorder());

        if (message.getOrigin().equals(message.getId())) {
            messagePanel.setBackground(Color.GREEN);
        } else {
            messagePanel.setBackground(Color.GRAY);
        }

        messagePanel.setMaximumSize(new Dimension(350, 30));

        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.add(messagePanel);

        return boxPanel;
    }

    private static JPanel messageInput(String message) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPanel.add(nachricht);
        return inputPanel;
    }

    private static JPanel displayRecentConversations(String[] userNames) {
        JPanel contactsPanel = new JPanel(new GridLayout(0, 1));
        for (String userName : userNames) {
            ImageIcon imageIconProfile = new ImageIcon("src/main/java/vs/chat/client/UI/profile.png");
            Image imageProfile = imageIconProfile.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH);
            JLabel profile = new JLabel(new ImageIcon(imageProfile));
            JLabel userNameLabel = new JLabel(userName);
            JPanel userPanel = new JPanel(new GridLayout(1, 2));
            userPanel.add(profile);
            userPanel.add(userNameLabel);
            userPanel.setBorder(BorderFactory.createEtchedBorder());
            contactsPanel.add(userPanel);

            MouseListener contactsMouseListener = new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    //TODO: Remove message creation when Backend is done!
                    Message message = new Message();
                    message.setContent("Hello World!");
                    message.setOrigin(UUID.fromString("16a9d592-79a7-4386-bbca-3a78fc091d34"));
                    System.out.println("Ich hab was gedrückt!");
                    rootPanel.getContentPane().removeAll();
                    rootPanel.getContentPane().add(header("Max Mustermann"), BorderLayout.NORTH);
                    rootPanel.getContentPane().add(displayNewMessage(message));
                    rootPanel.getContentPane().add(footer(), BorderLayout.SOUTH);
                    rootPanel.pack();
                }

                @Override
                public void mousePressed(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseReleased(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseEntered(MouseEvent mouseEvent) {

                }

                @Override
                public void mouseExited(MouseEvent mouseEvent) {

                }
            };
            userPanel.addMouseListener(contactsMouseListener);
        }

        return contactsPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    String[] nutzernamen = {"Max Mustermann", "Patrick Mischka", "Michael Angermeier", "Aaron Schweig", "Troy Kessler", "Matthias von End", "Jan Grübener"};
                    Message firstMessage = new Message();
                    firstMessage.setContent("Hello World!");
                    firstMessage.setOrigin(UUID.fromString("16a9d592-79a7-4386-bbca-3a78fc091d34"));
                    rootPanel = rootPanel();

                    rootPanel.getContentPane().add(displayRecentConversations(nutzernamen));

                    /*
                    rootPanel.getContentPane().add(header("Max Mustermann"), BorderLayout.NORTH);
                    rootPanel.getContentPane().add(displayNewMessage(firstMessage));
                    rootPanel.getContentPane().add(footer(), BorderLayout.SOUTH);
                    */


                    rootPanel.pack();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

}