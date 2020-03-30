package vs.chat.client;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.entities.User;
import vs.chat.packets.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

public class ClientApiImpl implements ClientApi {

    private Socket socket;
    private ObjectOutputStream networkOut;
    private ObjectInputStream networkIn;
    private BufferedReader userIn;

    private static SecretKeySpec secretKey;

    private UUID userId;
    private Set<Chat> chats;
    private Set<User> contacts;

    // 128 bit key generators
    private BigInteger n = new BigInteger("66259297998496367004492150774417033857");
    private BigInteger g = new BigInteger("29851");
    private BigInteger privateKey;

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

        this.privateKey = this.generatePrivateKey();
        System.out.println("Generated private key");
    }

    public UUID getUserId() {
        return userId;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    private BigInteger generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);

        return BigInteger.valueOf(ByteBuffer.wrap(bytes).getLong());
    }

    public void getChatMessages(UUID chatId) throws IOException {
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

    public void createChat(String chatName, final UUID... userIds) throws IOException {
        CreateChatPacket createChatPacket = new CreateChatPacket(chatName, userIds);

        this.networkOut.writeObject(createChatPacket);
        networkOut.flush();
    }

    public void sendMessage(String message, UUID chatId) throws IOException {
        MessagePacket messagePacket = new MessagePacket();
        messagePacket.content = encryptAES(chatId.toString(), message);
        messagePacket.target = chatId;

        networkOut.writeObject(messagePacket);
        networkOut.flush();
    }

    public void exit() throws IOException {
        socket.close();
        System.exit(0);
    }

    public String encryptAES(String key, String message) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public String decryptAES(String key, String ciffre) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciffre)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    public void setKey(String myKey) {
        MessageDigest sha;
        byte[] key;

        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
                            Set<Message> messages = ((GetMessagesResponsePacket) packet).messages;

                            messages.forEach(m -> m.setContent(decryptAES(m.getTarget().toString(), m.getContent())));

                            onChatMessages.run(new TreeSet<>(messages));
                        } else if (packet instanceof Message) {
                            Message message = (Message)packet;
                            message.content = decryptAES(message.target.toString(), message.getContent());

                            onMessage.run(message);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
