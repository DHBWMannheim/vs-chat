package vs.chat.client.UI;

import vs.chat.client.ClientApiImpl;
import vs.chat.entities.Chat;
import vs.chat.entities.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class ClientGUI {

    JFrame rootPanel;
    JTextArea messageInput;
    String[] nutzernamen = {"Max Mustermann", "Patrick Mischka", "Michael Angermeier", "Aaron Schweig", "Troy Kessler", "Matthias von End", "Jan Grübener"};

    // login, nachrichten senden usw
    private ClientApiImpl api;

    public ClientGUI(ClientApiImpl api) {
        this.api = api;
        this.startGui();
    }

    private JFrame rootPanel() {
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

    private JLabel getImageJLabel(String filename, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(filename);
        Image image = imageIcon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        JLabel imageJLabel = new JLabel(new ImageIcon(image));
        return imageJLabel;
    }

    private JPanel footer() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        messageInput = new JTextArea(2, 27);
        messageInput.setLineWrap(true);
        JLabel emojiJLabel = getImageJLabel("src/main/java/vs/chat/client/UI/emoji.png", 30, 30);
        JLabel sendLabel = getImageJLabel("src/main/java/vs/chat/client/UI/send.png", 30, 30);
        footerPanel.add(emojiJLabel);
        footerPanel.add(messageInput);
        footerPanel.add(sendLabel);
        footerPanel.setBorder(BorderFactory.createEtchedBorder());

        MouseListener emojiMouseListener = new MouseListener() {
            int counter = 0;
            JPanel emojiPanel;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getSource() == emojiJLabel) {
                    if (counter % 2 == 0) {
                        emojiPanel = renderEmojiPanel();
                        emojiPanel.setVisible(true);
                        JPanel emojiAppendFooterPanel = new JPanel(new BorderLayout());
                        emojiAppendFooterPanel.add(footerPanel, BorderLayout.SOUTH);
                        emojiAppendFooterPanel.add(emojiPanel, BorderLayout.NORTH);
                        rootPanel.getContentPane().add(emojiAppendFooterPanel, BorderLayout.SOUTH);
                        rootPanel.pack();
                        counter++;
                    } else {
                        emojiPanel.setVisible(false);
                        rootPanel.getContentPane().remove(emojiPanel);
                        emojiPanel = null;
                        counter++;
                    }
                } else if (e.getSource() == sendLabel) {
                    //TODO: Send Message to Server
                    System.out.println("Nachricht gesendet!");
                    messageInput.selectAll();
                    messageInput.replaceSelection("");
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
        emojiJLabel.addMouseListener(emojiMouseListener);
        sendLabel.addMouseListener(emojiMouseListener);
        return footerPanel;
    }

    private JPanel renderEmojiPanel() {
        float fontsize = 32f;
        JPanel emojiSelection = new JPanel(new GridLayout(2, 5));
        String[] unicodeemoji = {"\uD83D\uDE04", "\uD83D\uDE02", "\uD83D\uDE43", "\uD83D\uDE09", "\uD83D\uDE07", "\uD83D\uDE18", "\uD83D\uDE0B", "\uD83E\uDD14", "\uD83D\uDE0F", "\uD83D\uDE37"};
        for (int i = 1; i <= 10; i++) {
            EmojiMouseListener emojiMouseListener = new EmojiMouseListener(unicodeemoji[i - 1], messageInput);
            JLabel label = new JLabel(unicodeemoji[i - 1]);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(label.getFont().deriveFont(fontsize));
            label.addMouseListener(emojiMouseListener);
            emojiSelection.add(label);

        }
        JPanel centerEmojiPanel = new JPanel();
        centerEmojiPanel.setLayout(new BoxLayout(centerEmojiPanel, BoxLayout.Y_AXIS));
        centerEmojiPanel.add(emojiSelection);
        centerEmojiPanel.setBorder(BorderFactory.createEtchedBorder());

        return centerEmojiPanel;
    }

    private JPanel header(String username) {
        JPanel headerPanel = new JPanel(new GridLayout(0, 3));
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

        JLabel getBackToRecentContacts = getImageJLabel("src/main/java/vs/chat/client/UI/back.png", 60, 35);
        getBackToRecentContacts.addMouseListener(backMouseListener);
        headerPanel.add(getBackToRecentContacts);
        headerPanel.add(chatName);
        headerPanel.add(getImageJLabel("src/main/java/vs/chat/client/UI/profile.png", 40, 40));
        headerPanel.setBorder(BorderFactory.createEtchedBorder());
        return headerPanel;
    }

    private JScrollPane displayMessages() {
        //TODO: Implementation needed after Chat Entity is provided!
        return new JScrollPane();
    }

    private JPanel displayNewMessage(Message message) {
        JPanel messagePanel = new JPanel(new GridLayout(2, 0));
        JLabel userName = new JLabel(message.getOrigin().toString());
        JLabel content = new JLabel(message.getContent());
        messagePanel.add(userName);
        messagePanel.add(content);
        messagePanel.setBorder(BorderFactory.createEtchedBorder());

        if (message.getOrigin().equals(message.getId())) {
            messagePanel.setBackground(Color.DARK_GRAY);
        } else {
            messagePanel.setBackground(Color.GRAY);
        }
        messagePanel.setMaximumSize(new Dimension(350, 30));
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.add(messagePanel);
        return boxPanel;
    }

    private JPanel messageInput(String message) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPanel.add(nachricht);
        return inputPanel;
    }

    private JPanel loginPanel() {
        //JPanel rootLoginPanel = new JPanel(new BorderLayout());
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));


        loginPanel.add(new JLabel("Username: "));
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(550,20));
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Passwort: "));
        JPasswordField userPasswordField = new JPasswordField(20);
        userPasswordField.setMaximumSize(new Dimension(550,20));
        loginPanel.add(userPasswordField);

        JButton loginButton = new JButton("Login!");

        loginPanel.add(loginButton, BorderLayout.SOUTH);

        ActionListener loginButtonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO: Check credentials
                //String[] nutzernamen = {"Max Mustermann", "Patrick Mischka", "Michael Angermeier", "Aaron Schweig", "Troy Kessler", "Matthias von End", "Jan Grübener"};
                String username = usernameField.getText();
                char[] passwordCharArray = userPasswordField.getPassword();
                String password = "";

                for(char c : passwordCharArray){
                    password += c;
                }

                login(username, password);

                System.out.println(username +" "+ password);

            }
        }  ;
        loginButton.addActionListener(loginButtonListener);

        return loginPanel;
    }

    private void login(String username, String password){

        try{
            this.api.login(username, password);
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        if(api.getUserId() != null) {
            rootPanel.getContentPane().removeAll();
            rootPanel.getContentPane().add(displayRecentConversations(api.getChats()));
            rootPanel.pack();
        }

    }

    private JPanel displayRecentConversations(Set<Chat> chats) {
        JPanel contactsPanel = new JPanel(new GridLayout(0, 1));
        for (Chat chat : chats) {
            JLabel userNameLabel = new JLabel(chat.getName());
            JPanel userPanel = new JPanel(new GridLayout(1, 2));
            userPanel.add(getImageJLabel("src/main/java/vs/chat/client/UI/profile.png", 50, 50));
            userPanel.add(userNameLabel);
            userPanel.setBorder(BorderFactory.createEtchedBorder());
            contactsPanel.add(userPanel);
            MouseListener contactsMouseListener = new MouseListener() {

                @Override
                public void mouseClicked(MouseEvent mouseEvent) {
                    //TODO: Remove message creation when Backend is done!
                    Message message = new Message(UUID.fromString("16a9d592-79a7-4386-bbca-3a78fc091d34"));
                    message.setContent("Hello World!");
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

    private void startGui() {
        String[] nutzernamen = {"Max Mustermann", "Patrick Mischka", "Michael Angermeier", "Aaron Schweig", "Troy Kessler", "Matthias von End", "Jan Grübener"};
        Message message = new Message(UUID.fromString("16a9d592-79a7-4386-bbca-3a78fc091d34"));
        message.setContent("Hello World!");
        rootPanel = rootPanel();
        rootPanel.getContentPane().add(loginPanel());
        rootPanel.pack();
    }
}