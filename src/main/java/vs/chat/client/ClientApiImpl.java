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
import java.net.ConnectException;
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
    private List<String> nodes;
    private int hostIndex;

    private UUID userId;
    private Set<Chat> chats;
    private Set<User> contacts;

    // 128 bit key generators
    private BigInteger n = new BigInteger("238537765838013785000593286225584128587");
    private BigInteger g = new BigInteger("4176946703");
    private BigInteger privateKey;
    private BigInteger nextKey;
    private Keyfile keyfile;

    private boolean creatingChat = false;

    private String lastUsername;
    private String lastPassword;

    private static int PORT = 9876;
    private static int KEY_BYTE_LENGTH = 16;

    ClientApiImpl(List<String> nodes, BufferedReader userIn) {
        this.nodes = nodes;
        this.userIn = userIn;

        this.hostIndex = new Random().nextInt(nodes.size());
        String hostname = nodes.get(this.hostIndex);

        try {
            this.connect(hostname);
        } catch (ConnectException e) {
            this.reconnect();
        }
    }

    private void connect(String hostname) throws ConnectException {
        try {
            var port = PORT;
            if (hostname.contains(":")) {
                var split = hostname.split(":");
                hostname = split[0];
                port = Integer.parseInt(split[1]);
            }
            this.socket = new Socket(hostname, port);
            this.networkOut = new ObjectOutputStream(this.socket.getOutputStream());
            this.networkIn = new ObjectInputStream(this.socket.getInputStream());

            System.out.println("Connected to " + hostname + "\n");
        } catch (ConnectException e) {
            throw new ConnectException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void reconnect() {
        System.out.println("\nLost connection to " + this.nodes.get(this.hostIndex));
        while (true) {
            int newHostIndex = (this.hostIndex + 1) % this.nodes.size();
            String hostname = this.nodes.get(newHostIndex);
            System.out.println("\nReconnecting to " + hostname + "...");

            try {
                this.connect(hostname);
                break;
            } catch (ConnectException e) {
                try {
                    System.out.println(hostname + " not available");
                    Thread.sleep(5000);
                } catch (InterruptedException i) {
                    e.printStackTrace();
                }
            }
        }

        if (this.lastUsername != null) {
            try {
                this.login(this.lastUsername, this.lastPassword);
                System.out.println("Logged in as '" + this.lastUsername + "'");
                System.out.print("> ");
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }


    }

    public BufferedReader getUserIn() {
        return this.userIn;
    }

    public void login(String username, String password) throws LoginException {
        try {
            LoginPacket loginPacket = new LoginPacket(username, password);

            this.networkOut.writeObject(loginPacket);
            this.networkOut.flush();

            Object response = this.networkIn.readObject();

            if (response instanceof NoOpPacket) {
                throw new LoginException();
            }

            LoginSyncPacket loginSyncPacket = (LoginSyncPacket)response;

            this.lastUsername = username;
            this.lastPassword = password;

            this.userId = loginSyncPacket.userId;
            this.chats = loginSyncPacket.chats;
            this.contacts = loginSyncPacket.users;

            this.keyfile = new Keyfile(username);
            keyfile.load();
            keyfile.save();

            this.generatePrivateKey();
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

    private void generatePrivateKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[KEY_BYTE_LENGTH];
        random.nextBytes(bytes);

        this.privateKey = BigInteger.valueOf(ByteBuffer.wrap(bytes).getLong()).abs();
        this.nextKey = this.g.modPow(this.privateKey, this.n);
        System.out.println("\nGenerated private key: " + this.privateKey);
    }

    public void getChatMessages(UUID chatId) throws IOException {
        GetMessagesPacket getMessagesPacket = new GetMessagesPacket(chatId);

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

    public void exchangeKeys(String chatName, List<UUID> userIds, OnTimeout onTimeout) throws IOException, InterruptedException {
    	userIds.add(0, this.userId);
    	KeyExchangePacket keyEchangePacket = new KeyExchangePacket(
    		this.nextKey,
    		1,
    		this.userId,
    		userIds,
    		chatName,
    		userIds.get(1));


        this.networkOut.writeObject(keyEchangePacket);
        this.networkOut.flush();
        this.creatingChat = true;

        Thread.sleep(10000);

        if (this.creatingChat) {
            this.creatingChat = false;
            onTimeout.run();
        }
    }

    private void createChat(String chatName, List<UUID> userIds) throws IOException {
        UUID[] chatUsers = new UUID[userIds.size()];
        chatUsers = userIds.toArray(chatUsers);

        CreateChatPacket createChatPacket = new CreateChatPacket(chatName, chatUsers);
        this.networkOut.writeObject(createChatPacket);
        this.networkOut.flush();
    }

    public void sendMessage(String message, UUID chatId) throws IOException {
    	String chatKey = loadKey(chatId).toString();
        MessagePacket messagePacket = new MessagePacket(chatId, encryptAES(chatKey, message));

        this.networkOut.writeObject(messagePacket);
        this.networkOut.flush();
    }

    public String encryptAES(String key, String message) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, setKey(key));
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptAES(String key, String ciffre) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, setKey(key));
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciffre.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //Formatting key to SerectKeySpec
    public SecretKeySpec setKey(String myKey) {
        MessageDigest sha;
        byte[] key;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-256");
            key = sha.digest(key);
            key = Arrays.copyOf(key, KEY_BYTE_LENGTH);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
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


                            if (base.getBaseEntity() instanceof Chat) {
                                creatingChat = false;
                                Chat newChat = (Chat) base.getBaseEntity();


                                addKey(newChat.getId(), nextKey);
                                generatePrivateKey();
                                nextKey = g.modPow(privateKey, n);

                                chats.add(newChat);
                                onCreateChat.run(newChat);
                            } else if (base.getBaseEntity() instanceof Message) {
                                Message m = (Message) base.getBaseEntity();

                                Message d = new Message(m.getOrigin(), m.getReceiveTime());
                                d.setTarget(m.getTarget());

                                String chatKey = loadKey(m.getTarget()).toString();
                                d.setContent(decryptAES(chatKey, m.getContent()));

                                onMessage.run(d);
                            } else if (base.getBaseEntity() instanceof User) {
                                User u = (User) base.getBaseEntity();
                                contacts.add(u);
                                System.out.println("Added new User '" + u.getUsername() + "'");
                                System.out.print("> ");
                            }

                        } else if (packet instanceof KeyExchangePacket) {

                            KeyExchangePacket keyEchangePacket = (KeyExchangePacket) packet;
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
                                KeyExchangePacket newExchangePacket = new KeyExchangePacket(
                                		nextKey,
                                		keyEchangePacket.getRequests() + 1,
                                		keyEchangePacket.getInitiator(),
                                		keyEchangePacket.getParticipants(),
                                		keyEchangePacket.getChatName(),
                                		participants.get((userIndex + 1) % participants.size()));
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
                            Set<Message> messages = ((GetMessagesResponsePacket) packet).getMessages();
                            Set<Message> decrypted = new TreeSet<>();

                            for (Message m: messages) {
                                Message d = new Message(m.getOrigin(), m.getReceiveTime());
                                d.setTarget(m.getTarget());

                                String chatKey = loadKey(m.getTarget()).toString();
                                d.setContent(decryptAES(chatKey, m.getContent()));

                                decrypted.add(d);
                            }

                            onChatMessages.run(decrypted);
                        } else if (packet instanceof LogoutSuccessPacket) {
                            break;
                        }
                    } catch (EOFException e) {
                        reconnect();
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
