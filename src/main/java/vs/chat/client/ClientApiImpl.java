package vs.chat.client;

import vs.chat.entities.Chat;
import vs.chat.entities.User;
import vs.chat.packets.CreateChatPacket;
import vs.chat.packets.LoginPacket;
import vs.chat.packets.LoginSyncPacket;
import vs.chat.packets.MessagePacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Set;
import java.util.UUID;

public class ClientApiImpl implements ClientApi {

    private Socket socket;
    private ObjectOutputStream networkOut;
    private ObjectInputStream networkIn;

    private UUID userId;
    private Set<Chat> chats;
    private Set<User> contacts;

    ClientApiImpl(Socket socket, ObjectOutputStream networkOut, ObjectInputStream networkIn) {
        this.networkOut = networkOut;
        this.networkIn = networkIn;
        this.socket = socket;
    }

    // returns me id (uuid), contacts (uuid),
    public void login(String username, String password) throws IOException, ClassNotFoundException {
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.username = username;
        loginPacket.password = password;

        this.networkOut.writeObject(loginPacket);
        this.networkOut.flush();

        LoginSyncPacket response = (LoginSyncPacket)networkIn.readObject();

        this.userId = response.userId;
        this.chats = response.chats;
        this.contacts = response.users;
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public Set<User> getContacts() {
        return contacts;
    }

    // create chats with chat name and contacts (userIds)
    public Chat createChat(String chatName, final UUID... userIds) throws IOException, ClassNotFoundException {
        CreateChatPacket createChatPacket = new CreateChatPacket(chatName, userIds);

        this.networkOut.writeObject(createChatPacket);
        Chat createdChat = (Chat)networkIn.readObject();

        this.chats.add(createdChat);

        return createdChat;
    }

    // send chat message to user
    public void sendMessage(String message, UUID chatId) throws IOException {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.content = message;
        messagePacket.target = chatId;

        networkOut.writeObject(messagePacket);
        networkOut.flush();
    }

    public Object waitForMessages() throws IOException, ClassNotFoundException {
        return this.networkIn.readObject();
    }

    // exit program and close connection to server
    public void exit() throws IOException {
        socket.close();
        System.exit(0);
    }

}