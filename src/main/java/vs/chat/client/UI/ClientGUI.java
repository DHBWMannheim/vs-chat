package vs.chat.client.UI;

import vs.chat.client.ClientApiImpl;
import vs.chat.client.exceptions.LoginException;
import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.entities.User;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ClientGUI {

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

    private class CreateChatMouseListener implements MouseListener {

        User user;
        List<UUID> users = new ArrayList<>();
        JPanel contactsPanel;
        JPanel header;

        CreateChatMouseListener(JPanel contactsPanel, JPanel header) {
            this.contactsPanel = contactsPanel;
            this.header = header;
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            JPanel backround = new JPanel(new GridLayout(0, 1));
            JPanel newChatPanel = new JPanel(new GridLayout(0, 2));
            JLabel chatNameLabel = new JLabel("Chatname: ");
            JTextField chatNameField = new JTextField("");
            JLabel numberOfUsers = new JLabel("Number of useres to add: ");
            JFormattedTextField numberOfUserField = new JFormattedTextField(NumberFormat.getNumberInstance());
            JButton okButton = new JButton("ok!");
            okButton.addActionListener(new addUserToChatActionListener(chatNameField, numberOfUserField, header, contactsPanel));

            backround.add(header);
            newChatPanel.add(chatNameLabel);
            newChatPanel.add(chatNameField);
            newChatPanel.add(numberOfUsers);
            newChatPanel.add(numberOfUserField);
            backround.add(newChatPanel);
            backround.add(okButton);
            contactsPanel.removeAll();
            rootPanel.pack();
            contactsPanel.add(backround);
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

    private class addUserToChatActionListener implements ActionListener {
        User user;
        List<UUID> users;
        JTextField chatnameTextField;
        JFormattedTextField useramountTextField;
        JPanel header;
        JPanel contactsPanel;
        String chatname;
        long useramount;
        ArrayList<JTextField> addedUsers = new ArrayList<>();

        addUserToChatActionListener(JTextField chatnameTextField, JFormattedTextField useramountTextField, JPanel header, JPanel contactsPanel) {
            this.chatnameTextField = chatnameTextField;
            this.useramountTextField = useramountTextField;
            this.header = header;
            this.contactsPanel = contactsPanel;
            users = new ArrayList<>();
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            chatname = chatnameTextField.getText();
            useramount = (long) useramountTextField.getValue();
            JPanel backround = new JPanel(new GridLayout(0, 1));
            JPanel addUserNamePanel = new JPanel(new GridLayout(0, 2));
            backround.add(header);
            for (long i = useramount; i > 0; i--) {
                JLabel userNameLabel = new JLabel(i + " User (username): ");
                JTextField userNameField = new JTextField();
                addUserNamePanel.add(userNameLabel);
                addUserNamePanel.add(userNameField);
                addedUsers.add(userNameField);
            }

            JButton okButton = new JButton("ok!");
            backround.add(addUserNamePanel);
            backround.add(okButton);
            contactsPanel.removeAll();
            rootPanel.pack();
            contactsPanel.add(backround);
            rootPanel.pack();

            okButton.addActionListener(new CreateChatInBackendActionListener(chatname, addedUsers));
        }
    }

    private class SendMouseListener implements MouseListener {


        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                api.sendMessage(messageInput.getText(), currentChat.getId());
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
        private JTextField usernameField;
        private JPasswordField userPasswordField;

        public LoginButtonActionListener(JTextField usernameField, JPasswordField userPasswordField) {
            this.usernameField = usernameField;
            this.userPasswordField = userPasswordField;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            try {
                api.login(usernameField.getText(), new String(userPasswordField.getPassword()));
            } catch (LoginException e) {
                JOptionPane.showMessageDialog(rootPanel, "Invalid Username or Password");
                e.printStackTrace();
            }
            if (api.getUserId() != null) {
                System.out.println(usernameField.getText() + " " + new String(userPasswordField.getPassword()));
                api.startPacketListener(ClientGUI.this::onCreateChat, ClientGUI.this::onGetMessageHistory, ClientGUI.this::onMessage);
                rootPanel.getContentPane().removeAll();
                rootPanel.getContentPane().add(displayRecentConversations());
                rootPanel.pack();
            }
        }
    }

    private class CreateChatInBackendActionListener implements ActionListener {

        User user;
        String chatname;
        ArrayList<JTextField> addedUsers;
        List<UUID> users = new ArrayList<>();

        CreateChatInBackendActionListener(String chatname, ArrayList<JTextField> addedUsers) {
            this.chatname = chatname;
            this.addedUsers = addedUsers;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            for (JTextField textField : addedUsers) {
                user = api.getContacts().stream().filter(c -> c.getUsername().equals(textField.getText())).findAny().orElse(null);
                if (user == null) {
                    JOptionPane.showMessageDialog(rootPanel, "User not found!");
                }
                users.add(user.getId());
            }
            try {
                api.exchangeKeys(chatname, users, this::onTimeout);
            } catch (IOException | InterruptedException e) {
                JOptionPane.showMessageDialog(rootPanel, "Es ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut!");
                e.printStackTrace();
            }
        }

        private void onTimeout() {
            JOptionPane.showMessageDialog(rootPanel, "Chat konnte nicht erstellt werden. Einer der Nutzer ist offline. Bitte versuchen Sie es später erneut!");
            rootPanel.getContentPane().removeAll();
            rootPanel.getContentPane().add(displayRecentConversations());
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
        JTextField usernameField = new JTextField(20);
        usernameField.setMaximumSize(new Dimension(550, 20));
        loginPanel.add(usernameField);

        loginPanel.add(new JLabel("Passwort: "));
        JPasswordField userPasswordField = new JPasswordField(20);
        userPasswordField.setMaximumSize(new Dimension(550, 20));
        loginPanel.add(userPasswordField);

        JButton loginButton = new JButton("Login!");
        loginPanel.add(loginButton, BorderLayout.SOUTH);
        loginButton.addActionListener(new LoginButtonActionListener(usernameField, userPasswordField));

        return loginPanel;
    }

    private void onMessage(Message message) {
        try {
            api.getChatMessages(currentChat.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onCreateChat(Chat chat) {
        rootPanel.getContentPane().removeAll();
        rootPanel.getContentPane().add(displayRecentConversations());
        rootPanel.pack();
        System.out.println("Sie wurden zu einem neuen Chat hinzugefügt");
    }

    private void onGetMessageHistory(Set<Message> messages) {
        rootPanel.getContentPane().removeAll();
        rootPanel.getContentPane().add(header(currentChat), BorderLayout.NORTH);

        JPanel messagePanel = new JPanel(new GridLayout(0, 1));
        for (Message message : messages) {
            System.out.println(message.getContent());
            JPanel messageContentPanel = new JPanel();
            messageContentPanel.setLayout(new BorderLayout());
            if (message.getOrigin().equals(api.getUserId())){
                messageContentPanel.add(new JLabel(message.getContent()),BorderLayout.EAST);
                messageContentPanel.setBackground(new Color(158, 200, 145));
            }else {
                messageContentPanel.add(new JLabel(message.getContent()),BorderLayout.WEST);
                messageContentPanel.setBorder(BorderFactory.createEtchedBorder());
                messageContentPanel.setBackground(new Color(120, 120, 120));
            }
            messagePanel.add(messageContentPanel);
        }

        JScrollPane chatJScrollPane = new JScrollPane(messagePanel);
        chatJScrollPane.setLayout(new ScrollPaneLayout());
        chatJScrollPane.setVerticalScrollBar(new JScrollBar());


        rootPanel.add(chatJScrollPane, BorderLayout.CENTER);
        rootPanel.getContentPane().add(footer(), BorderLayout.SOUTH);
        rootPanel.pack();
    }

    public JPanel displayRecentConversations() {
        JPanel chatsPanel = new JPanel(new GridLayout(0, 1));
        JPanel newChatPanel = new JPanel(new GridLayout(0, 2));
        JLabel titleLabel = new JLabel("VS-Chat");
        titleLabel.setForeground(Color.WHITE);
        newChatPanel.add(titleLabel);
        JLabel addChatLabel = getImageJLabel("src/main/java/vs/chat/client/UI/icons/add.png", 50, 50);
        addChatLabel.addMouseListener(new CreateChatMouseListener(chatsPanel, newChatPanel));
        newChatPanel.add(addChatLabel);
        newChatPanel.setBackground(Color.DARK_GRAY);
        newChatPanel.setMaximumSize(new Dimension(600, 100));
        chatsPanel.add(newChatPanel);

        for (Chat chat : api.getChats()) {
            JLabel nameLabel = new JLabel(chat.getName());
            JPanel panel = new JPanel(new GridLayout(1, 2));
            panel.add(getImageJLabel("src/main/java/vs/chat/client/UI/icons/profile.png", 50, 50));
            panel.add(nameLabel);
            panel.setBorder(BorderFactory.createEtchedBorder());
            chatsPanel.add(panel);
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
                rootPanel = rootPanel();
                rootPanel.getContentPane().add(loginPanel());
                rootPanel.pack();
            }
        });

    }
}