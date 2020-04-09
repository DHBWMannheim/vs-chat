package vs.chat.client;

import vs.chat.client.exceptions.LoginException;
import vs.chat.client.keyfile.Keyfile;
import vs.chat.client.keyfile.KeyfileResourceType;
import vs.chat.client.keyfile.PrivateKeyEntity;
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
    private BigInteger n = new BigInteger("238537765838013785000593286225584128587");
    private BigInteger g = new BigInteger("4176946703");
    private BigInteger privateKey;
    private BigInteger nextKey;
    private Keyfile keyfile;

    private static int KEY_BYTE_LENGTH = 16;

    ClientApiImpl(Socket socket, ObjectOutputStream networkOut, ObjectInputStream networkIn, BufferedReader userIn) {
        this.networkOut = networkOut;
        this.networkIn = networkIn;
        this.socket = socket;
        this.userIn = userIn;
    }

    public BufferedReader getUserIn() {
        return this.userIn;
    }

    public void login(String username, String password) throws LoginException {
        try {
            LoginPacket loginPacket = new LoginPacket();
            loginPacket.username = username;
            loginPacket.password = password;

            this.networkOut.writeObject(loginPacket);
            this.networkOut.flush();

            Object response = this.networkIn.readObject();

            if (response instanceof NoOpPacket) {
                throw new LoginException();
            }

            LoginSyncPacket loginSyncPacket = (LoginSyncPacket)response;

            this.userId = loginSyncPacket.userId;
            this.chats = loginSyncPacket.chats;
            this.contacts = loginSyncPacket.users;

            this.keyfile = new Keyfile(username);
            try{
                keyfile.load();
            }catch (ClassNotFoundException | IOException e){
                e.printStackTrace();
            }
            try {
                keyfile.save();
            }catch (IOException e1){
                e1.printStackTrace();
            }
            this.privateKey = generatePrivateKey();
            this.nextKey = this.g.modPow(this.privateKey, this.n);
            System.out.println(this.userId);
            System.out.println("\nGenerated private key: " + this.privateKey);

        } catch (IOException | ClassNotFoundException e) {
            throw new LoginException();
        }
    }

    public UUID getUserId() {
        return this.userId;
    }

    public Set<Chat> getChats() {
        return this.chats;
    }

    private BigInteger generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[KEY_BYTE_LENGTH];
        random.nextBytes(bytes);

        return BigInteger.valueOf(ByteBuffer.wrap(bytes).getLong()).abs();
    }

    public void getChatMessages(UUID chatId) throws IOException {
        GetMessagesPacket getMessagesPacket = new GetMessagesPacket();
        getMessagesPacket.chatId = chatId;

        this.networkOut.writeObject(getMessagesPacket);
        this.networkOut.flush();
    }

    public Set<User> getContacts() {
        return this.contacts
                .stream()
                .filter(c -> c.getId() != this.userId)
                .collect(Collectors.toSet());
    }

    public String getUsernameFromId(UUID id) {
        User user = this.contacts.stream()
                        .filter(c -> c.getId().equals(id))
                        .findAny()
                        .orElse(null);

        if (user != null) {
            return user.getUsername();
        }
        return null;
    }

    public void exchangeKeys(String chatName, List<UUID> userIds) throws IOException {
        KeyEchangePacket keyEchangePacket = new KeyEchangePacket();

        userIds.add(0, this.userId);

        keyEchangePacket.setTarget(userIds.get(1));
        keyEchangePacket.setContent(this.nextKey);
        keyEchangePacket.setRequests(1);
        keyEchangePacket.setInitiator(this.userId);
        keyEchangePacket.setChatName(chatName);
        keyEchangePacket.setParticipants(userIds);

        this.networkOut.writeObject(keyEchangePacket);
        this.networkOut.flush();
    }

    public void createChat(String chatName, List<UUID> userIds) throws IOException {
        UUID[] chatUsers = new UUID[userIds.size()];
        chatUsers = userIds.toArray(chatUsers);

        CreateChatPacket createChatPacket = new CreateChatPacket(chatName, chatUsers);
        this.networkOut.writeObject(createChatPacket);
        this.networkOut.flush();
    }

    public void sendMessage(String message, UUID chatId) throws IOException {
        MessagePacket messagePacket = new MessagePacket();
        String chatKey = loadKey(chatId).toString();
        messagePacket.content = encryptAES(chatKey, message);
        messagePacket.target = chatId;

        this.networkOut.writeObject(messagePacket);
        this.networkOut.flush();
    }

    public String encryptAES(String key, String message) {
        try {
            setKey(key);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
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

    //Formatting key to SerectKeySpec
    public void setKey(String myKey) {
        MessageDigest sha;
        byte[] key;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, KEY_BYTE_LENGTH);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void addKey(UUID chatId, BigInteger key) {
        var pKEntry = new PrivateKeyEntity(chatId, key);
        this.keyfile.get(KeyfileResourceType.PRIVATEKEY).put(chatId, pKEntry);
        try {
            keyfile.save();
        } catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public BigInteger loadKey(UUID chatId) {
        var res = keyfile.get(KeyfileResourceType.PRIVATEKEY).values().stream()
                .filter(u -> ((PrivateKeyEntity) u).equals(chatId)).findFirst();
        if (res.isPresent()) {
            PrivateKeyEntity pke = (PrivateKeyEntity) keyfile.get(KeyfileResourceType.PRIVATEKEY).get(chatId);
            try {
                keyfile.save();
            } catch (IOException e1){
                e1.printStackTrace();
            }
            return pke.getPrivateKey();
        }
        try {
            keyfile.save();
        } catch (IOException e1){
            e1.printStackTrace();
        }
        return null;
    }

    public void deleteKey(UUID chatId) {
        keyfile.get(KeyfileResourceType.PRIVATEKEY).remove(chatId);
        try {
            keyfile.save();
        }catch (IOException e1){
            e1.printStackTrace();
        }
    }

    public void exit() throws IOException {
        LogoutPacket logoutPacket = new LogoutPacket();
        this.networkOut.writeObject(logoutPacket);
        this.networkOut.flush();
    }

    public void startPacketListener(OnCreateChat onCreateChat, OnGetChatMessages onChatMessages, OnMessage onMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object packet = networkIn.readObject();

                        if (packet instanceof BaseEntityBroadcastPacket) {
                            BaseEntityBroadcastPacket base = (BaseEntityBroadcastPacket) packet;

                            if (base.baseEntity instanceof Chat) {
                                Chat newChat = (Chat) base.baseEntity;

                                addKey(newChat.getId(), nextKey);

                                chats.add(newChat);
                                onCreateChat.run(newChat);
                            } else if (base.baseEntity instanceof Message) {
                                Message message = (Message) base.baseEntity;

                                // load chat key and decrypt message
                                String chatKey = loadKey(message.getTarget()).toString();
                                message.content = decryptAES(chatKey, message.getContent());

                                onMessage.run(message);
                            }

                        } else if (packet instanceof KeyEchangePacket) {

                            KeyEchangePacket keyEchangePacket = (KeyEchangePacket) packet;
                            List<UUID> participants = keyEchangePacket.getParticipants();

                            int currentRequests = keyEchangePacket.getRequests();
                            BigInteger currentContent = keyEchangePacket.getContent();
                            int targetRequests = participants.size() * (participants.size() - 1);
                            int userIndex = participants.indexOf(userId);

                            int rounds = currentRequests / participants.size();

                            if (keyEchangePacket.getInitiator().equals(userId)) {
                                nextKey = currentContent.modPow(privateKey, n);
                            } else {
                                rounds++;
                            }

                            System.out.println("\n-> Exchanging keys round " + rounds);

                            // check if package has to be forwarded
                            if (currentRequests < targetRequests) {
                                // forwards package to next participant
                                KeyEchangePacket newExchangePacket = new KeyEchangePacket();
                                newExchangePacket.setTarget(participants.get((userIndex + 1) % participants.size()));
                                newExchangePacket.setContent(nextKey);
                                newExchangePacket.setRequests(keyEchangePacket.getRequests() + 1);
                                newExchangePacket.setInitiator(keyEchangePacket.getInitiator());
                                newExchangePacket.setParticipants(keyEchangePacket.getParticipants());
                                newExchangePacket.setChatName(keyEchangePacket.getChatName());

                                networkOut.writeObject(newExchangePacket);
                                networkOut.flush();
                            }

                            nextKey = currentContent.modPow(privateKey, n);

                            // check if participant has finished key exchange
                            if (keyEchangePacket.getInitiator().equals(userId)) {
                                if (currentRequests == (targetRequests - userIndex)) {
                                    System.out.println(getUsernameFromId(userId) + " -> " + nextKey);
                                    participants.remove(0);
                                    createChat(keyEchangePacket.getChatName(), participants);
                                }
                            } else if (currentRequests == (targetRequests - (participants.size() - userIndex))) {
                                System.out.println(getUsernameFromId(userId) + " -> " + nextKey);
                            }

                        } else if (packet instanceof GetMessagesResponsePacket) {
                            Set<Message> messages = ((GetMessagesResponsePacket) packet).messages;

                            // load chat key and decrypt message
                            messages.forEach(message -> {
                                String chatKey = loadKey(message.getTarget()).toString();
                                message.setContent(decryptAES(chatKey, message.getContent()));
                            });

                            onChatMessages.run(new TreeSet<>(messages));
                        } else if (packet instanceof LogoutSuccessPacket) {
                            break;
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                System.out.println("Logged out");
                System.exit(0);

            }
        }).start();
    }

}
