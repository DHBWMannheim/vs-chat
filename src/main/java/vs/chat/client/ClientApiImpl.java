package vs.chat.client;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.entities.User;
import vs.chat.packets.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class ClientApiImpl implements ClientApi {

    private Socket socket;
    private ObjectOutputStream networkOut;
    private ObjectInputStream networkIn;
    private BufferedReader userIn;

    private UUID userId;
    private Set<Chat> chats;
    private Set<User> contacts;

    ClientApiImpl(Socket socket, ObjectOutputStream networkOut, ObjectInputStream networkIn, BufferedReader userIn) {
        this.networkOut = networkOut;
        this.networkIn = networkIn;
        this.socket = socket;
        this.userIn = userIn;
    }

    public BufferedReader getUserIn() {
        return this.userIn;
    }

    public void login() throws IOException, ClassNotFoundException {
        Object response;

        do {
            System.out.print("Username: ");
            String username = this.userIn.readLine();

            System.out.print("Password: ");
            String password = this.userIn.readLine();

            LoginPacket loginPacket = new LoginPacket();
            loginPacket.username = username;
            loginPacket.password = password;

            this.networkOut.writeObject(loginPacket);
            this.networkOut.flush();

            response = networkIn.readObject();

            if (response instanceof NoOpPacket) {
                System.out.println("Password incorrect!");
            }

        } while (response instanceof NoOpPacket);

        LoginSyncPacket loginSyncPacket = (LoginSyncPacket)response;

        this.userId = loginSyncPacket.userId;
        this.chats = loginSyncPacket.chats;
        this.contacts = loginSyncPacket.users;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void getChatMessages(UUID chatId) throws IOException, ClassNotFoundException {
        GetMessagesPacket getMessagesPacket = new GetMessagesPacket();
        getMessagesPacket.chatId = chatId;

        this.networkOut.writeObject(getMessagesPacket);
        this.networkOut.flush();
    }

    public Set<User> getContacts() {
        return contacts
                .stream()
                .filter(c -> c.getId() != this.userId)
                .collect(Collectors.toSet());
    }

    public String getUsernameFromId(UUID userId) {
        User user = contacts.stream()
                        .filter(c -> c.getId() == userId)
                        .findAny()
                        .orElse(null);

        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public void createChat(String chatName, final UUID... userIds) throws IOException, ClassNotFoundException {
        CreateChatPacket createChatPacket = new CreateChatPacket(chatName, userIds);

        this.networkOut.writeObject(createChatPacket);
        networkOut.flush();
    }

    public void sendMessage(String message, UUID chatId) throws IOException {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.content = message;
        messagePacket.target = chatId;

        networkOut.writeObject(messagePacket);
        networkOut.flush();
    }

    public Message waitForNewMessages() throws IOException, ClassNotFoundException {
        Object response = this.networkIn.readObject();

        if (response instanceof Message) {
            return ((Message) response);
        }
        return null;
    }

    public void exit() throws IOException {
        socket.close();
        System.exit(0);
    }

    public void startPacketListener(OnCreateChat onCreateChat, OnGetChatMessages onChatMessages, OnMessage onMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object packet = networkIn.readObject();

                        if (packet instanceof Chat) {
                            chats.add((Chat)packet);
                            onCreateChat.run((Chat)packet);
                        } else if (packet instanceof GetMessagesResponsePacket) {
                            onChatMessages.run(new TreeSet<>(((GetMessagesResponsePacket) packet).messages));
                        } else if (packet instanceof Message) {
                            onMessage.run((Message)packet);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
