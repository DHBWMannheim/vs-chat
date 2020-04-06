package vs.chat.client.UI;

import vs.chat.client.ClientApiImpl;
import vs.chat.client.exceptions.LoginException;
import vs.chat.entities.Chat;
import vs.chat.entities.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClientGUI {

    // dummyData gets set in startGui(), to be replaced by data fetched by api
    Set<Message> dummyMessages;
    Set<Chat> dummyChats;

    Chat currentChat;

    JFrame rootPanel;
    JTextArea messageInput;

    // login, nachrichten senden usw
    private ClientApiImpl api;

    public ClientGUI(ClientApiImpl api) {
        this.api = api;
        this.startGui();
    }

    private class BackMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            JPanel recentConversations = displayRecentConversations();
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
    }

    private class ContactsMouseListener implements MouseListener {

        private Chat chat;

        public ContactsMouseListener(Chat chat) {
            this.chat = chat;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Ich bin der Listener für : " + chat.getName());
            currentChat = chat;
            try {
                System.out.println("getting chat messages...");
                api.getChatMessages(chat.getId());
            } catch (IOException ex) {
                ex.printStackTrace();
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
    }

    private class SelectEmojiMouseListener implements MouseListener {
        String unicode;

        public SelectEmojiMouseListener(String unicode) {
            this.unicode = unicode;
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            messageInput.append(unicode);
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
    }

    private class OpenEmojiPanelMouseListener implements MouseListener {

        private JPanel footerPanel;
        private JPanel emojiPanel;
        private int counter;

        JPanel emojiAppendFooterPanel;

        public OpenEmojiPanelMouseListener(JPanel footerPanel) {


            this.footerPanel = footerPanel;
            this.emojiPanel = new JPanel();


            emojiAppendFooterPanel = new JPanel(new BorderLayout());
            emojiAppendFooterPanel.add(footerPanel, BorderLayout.SOUTH);
            emojiAppendFooterPanel.add(emojiPanel, BorderLayout.NORTH);

            counter = 0;
        }

        @Override
        public void mouseClicked(MouseEvent e) {

            if (counter % 2 == 0) {
                emojiPanel = renderEmojiPanel();
                emojiPanel.setVisible(true);
                emojiAppendFooterPanel = new JPanel(new BorderLayout());
                emojiAppendFooterPanel.add(footerPanel, BorderLayout.SOUTH);
                emojiAppendFooterPanel.add(emojiPanel, BorderLayout.NORTH);
                rootPanel.getContentPane().add(emojiAppendFooterPanel, BorderLayout.SOUTH);
                rootPanel.pack();
                counter++;
            } else {
                emojiPanel.setVisible(false);
                rootPanel.getContentPane().remove(emojiAppendFooterPanel);
                rootPanel.getContentPane().add(footerPanel, BorderLayout.SOUTH);
                emojiAppendFooterPanel = null;
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
    }

    private class SendMouseListener implements MouseListener {


        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                api.sendMessage(messageInput.toString(), UUID.randomUUID());
            } catch (IOException ex) {
                ex.printStackTrace();
            }


            System.out.println("Nachricht gesendet!");
            messageInput.selectAll();
            messageInput.replaceSelection("");
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
    }

    private class LoginButtonActionListener implements ActionListener {
        private String userName;
        private String password;

        public LoginButtonActionListener(String userName, String password) {
            this.userName = userName;
            this.password = password;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                api.login(userName, password);
            } catch (LoginException e) {
                JOptionPane.showMessageDialog(rootPanel, "Invalid Username or Password");
                e.printStackTrace();
            }
            if (api.getUserId() != null) {
                System.out.println(userName + " " + password);
                api.startPacketListener(this::onCreateChat, this::onGetMessageHistory, this::onMessage);
                rootPanel.getContentPane().removeAll();
                rootPanel.getContentPane().add(displayRecentConversations());
                rootPanel.pack();
            }
        }

        private void onMessage(Message message) {
            System.out.println("Neue Nachricht geht ein!");
        }

        private void onCreateChat(Chat chat) {
            System.out.println("Erstelle einen neuen Chat!");
        }

        private void onGetMessageHistory(Set<Message> messages) {
            System.out.println(messages);
            rootPanel.getContentPane().removeAll();
            rootPanel.getContentPane().add(header(currentChat), BorderLayout.NORTH);

            Set<Message> chatMessages = new HashSet<>();
            for (Message message : messages) {
                if (message.getOrigin().equals(currentChat.getId())) {
                    chatMessages.add(message);
                }
            }

            JScrollPane chatJScrollPane = new JScrollPane();
            chatJScrollPane.setLayout(new ScrollPaneLayout());
            chatJScrollPane.setVerticalScrollBar(new JScrollBar());

            JPanel testPanel = new JPanel();
            testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.Y_AXIS));

            int i = 0;
            for (Message message : chatMessages) {
                message.setContent(message.content);
                testPanel.add(displayNewMessage(message), i++);
                System.out.println("Entered for in displayMessages: " + i);
            }
            chatJScrollPane.add(testPanel);
            rootPanel.add(chatJScrollPane, BorderLayout.CENTER);
            rootPanel.getContentPane().add(footer(), BorderLayout.SOUTH);
            rootPanel.pack();
        }

    }

    private JFrame rootPanel() {
        JFrame frame = new JFrame();
        Image logo = Toolkit.getDefaultToolkit().getImage("src/main/java/vs/chat/client/UI/icons/mario.png");
        frame.setIconImage(logo);
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 600));
        frame.setVisible(true);
        frame.setResizable(false);
        frame.pack();
        return frame;
    }

    private JPanel loginPanel() {
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        loginPanel.add(new JLabel("Username: "));
        JTextField usernameField = new JTextField("troy", 20);
        usernameField.setMaximumSize(new Dimension(550, 20));
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Passwort: "));
        JPasswordField userPasswordField = new JPasswordField("asdf", 20);
        userPasswordField.setMaximumSize(new Dimension(550, 20));
        loginPanel.add(userPasswordField);

        JButton loginButton = new JButton("Login!");
        loginPanel.add(loginButton, BorderLayout.SOUTH);
        loginButton.addActionListener(new LoginButtonActionListener(usernameField.getText(), new String(userPasswordField.getPassword())));

        return loginPanel;
    }

    public JPanel displayRecentConversations() {
        JPanel chatsPanel = new JPanel(new GridLayout(0, 1));
        for (Chat chat : api.getChats()) {
            System.out.println(chat.getName());
        }
        for (Chat chat : api.getChats()) {
            System.out.println(chat + chat.getName());
            JLabel nameLabel = new JLabel(chat.getName());
            JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.add(getImageJLabel("src/main/java/vs/chat/client/UI/icons/profile.png", 50, 50));
            panel.add(nameLabel);
            panel.setBorder(BorderFactory.createEtchedBorder());
            chatsPanel.add(panel);
            // Alle Chats sind angezeigt

            panel.addMouseListener(new ContactsMouseListener(chat));
        }
        return chatsPanel;
    }

    private JPanel header(Chat chat) {
        //dummyChats.stream().filter(c -> c.getName().equals("dummyChat1")).findAny().orElse(null).getName() : Abfrage eines Chats aus allen Chats.
        JPanel headerPanel = new JPanel(new GridLayout(0, 3));
        JLabel chatName = new JLabel(chat.getName());

        JLabel getBackToRecentContacts = getImageJLabel("src/main/java/vs/chat/client/UI/icons/back.png", 60, 35);
        getBackToRecentContacts.addMouseListener(new BackMouseListener());
        headerPanel.add(getBackToRecentContacts);
        headerPanel.add(chatName);
        headerPanel.add(getImageJLabel("src/main/java/vs/chat/client/UI/icons/profile.png", 40, 40));
        headerPanel.setBorder(BorderFactory.createEtchedBorder());
        return headerPanel;
    }

    private JPanel footer() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        messageInput = new JTextArea(2, 27);
        messageInput.setLineWrap(true);
        JLabel emojiJLabel = getImageJLabel("src/main/java/vs/chat/client/UI/icons/emoji.png", 30, 30);
        JLabel sendLabel = getImageJLabel("src/main/java/vs/chat/client/UI/icons/send.png", 30, 30);
        footerPanel.add(emojiJLabel);
        footerPanel.add(messageInput);
        footerPanel.add(sendLabel);
        footerPanel.setBorder(BorderFactory.createEtchedBorder());

        emojiJLabel.addMouseListener(new OpenEmojiPanelMouseListener(footerPanel));
        sendLabel.addMouseListener(new SendMouseListener());

        return footerPanel;
    }

    private JPanel messageInput(String message) {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel nachricht = new JLabel(message);
        inputPanel.add(nachricht);
        return inputPanel;
    }

    private JPanel displayNewMessage(Message message) {
        JPanel messagePanel = new JPanel(new GridLayout(2, 0));
        JLabel userName = new JLabel(message.getOrigin().toString());
        JLabel content = new JLabel(message.getContent());
        System.out.println("displayNewMessage triggerd: " + message.getOrigin());
        messagePanel.add(userName);
        messagePanel.add(content);
        messagePanel.setBorder(BorderFactory.createEtchedBorder());
    /*
        if (message.getOrigin().equals(message.getId())) {
            messagePanel.setBackground(Color.DARK_GRAY);
        } else {
            messagePanel.setBackground(Color.GRAY);
        }
        messagePanel.setMaximumSize(new Dimension(350, 30));
        JPanel boxPanel = new JPanel();
        boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));
        boxPanel.add(messagePanel);
        */
        return messagePanel;
    }

    public JPanel renderEmojiPanel() {
        float fontsize = 32f;
        JPanel emojiSelection = new JPanel(new GridLayout(2, 5));
        String[] unicodeemoji = {"\uD83D\uDE04", "\uD83D\uDE02", "\uD83D\uDE43", "\uD83D\uDE09", "\uD83D\uDE07", "\uD83D\uDE18", "\uD83D\uDE0B", "\uD83E\uDD14", "\uD83D\uDE0F", "\uD83D\uDE37"};
        for (int i = 1; i <= 10; i++) {
            JLabel label = new JLabel(unicodeemoji[i - 1]);
            label.setHorizontalAlignment(JLabel.CENTER);
            label.setFont(label.getFont().deriveFont(fontsize));
            label.addMouseListener(new SelectEmojiMouseListener(unicodeemoji[i - 1]));
            emojiSelection.add(label);
        }
        JPanel centerEmojiPanel = new JPanel();
        centerEmojiPanel.setLayout(new BoxLayout(centerEmojiPanel, BoxLayout.Y_AXIS));
        centerEmojiPanel.add(emojiSelection);
        centerEmojiPanel.setBorder(BorderFactory.createEtchedBorder());

        return centerEmojiPanel;
    }

    private JLabel getImageJLabel(String filename, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(filename);
        Image image = imageIcon.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        JLabel imageJLabel = new JLabel(new ImageIcon(image));
        return imageJLabel;
    }

    private void startGui() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                //TODO: Remove and use API methods when Bugs is fixed

                /*dummyChats = new HashSet<>();

                UUID uuid1 = UUID.randomUUID();
                UUID uuid2 = UUID.randomUUID();
                UUID uuid3 = UUID.randomUUID();
                UUID uuid4 = UUID.randomUUID();

                dummyChats.add(new Chat("dummyChat1", uuid1, uuid2));
                dummyChats.add(new Chat("dummyChat2", uuid2, uuid3));
                dummyChats.add(new Chat("dummyChat3", uuid3, uuid4));
                Message message1 = new Message(dummyChats.stream().filter(c -> c.getName().equals("dummyChat1")).findAny().orElse(null).getId());
                Message message2 = new Message(dummyChats.stream().filter(c -> c.getName().equals("dummyChat1")).findAny().orElse(null).getId());
                Message message3 = new Message(dummyChats.stream().filter(c -> c.getName().equals("dummyChat1")).findAny().orElse(null).getId());
                Message message4 = new Message(dummyChats.stream().filter(c -> c.getName().equals("dummyChat1")).findAny().orElse(null).getId());

                message1.setContent("Nachricht1!!");
                message2.setContent("Ich bin eine zweite Nachricht!");
                message3.setContent("hihi");
                message4.setContent("How much is the fish?");

                dummyMessages = new HashSet<>();
                dummyMessages.add(message1);
                dummyMessages.add(message2);
                dummyMessages.add(message3);
                dummyMessages.add(message4);
                 */
                rootPanel = rootPanel();
                rootPanel.getContentPane().add(loginPanel());
                rootPanel.pack();


            }
        });

    }
}