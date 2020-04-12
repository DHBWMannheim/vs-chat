package vs.chat.client;

import vs.chat.client.exceptions.LoginException;
import vs.chat.entities.Chat;
import vs.chat.entities.User;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ClientApi {

    // login with username and password, returns user id, user chat & all users
    void login(String username, String password) throws LoginException;

    // get user id of current user
    UUID getUserId();

    // get all chats of current user
    Set<Chat> getChats();

    // get all messages of chat sorted in chronological order
    void getChatMessages(UUID chatId) throws IOException, ClassNotFoundException;

    // gets all users except current user
    Set<User> getContacts();

    // gets username of contact id
    String getUsernameFromId(UUID userId);

    // exchange key between clients
    void exchangeKeys(String chatName, List<UUID> userIds, OnTimeout onTimeout) throws IOException, InterruptedException;

    // send chat message to user
    void sendMessage(String message, UUID chatId) throws IOException;

    // exit program and close connection to server
    void exit() throws IOException;

    // encrypt message with AES
    String encryptAES(String key, String message);

    // decrypt ciffre with AES
    String decryptAES(String key, String ciffre);

    // generate Key for AES
    SecretKeySpec setKey(String myKey);

    void addKey(UUID chatId, BigInteger key);

    BigInteger loadKey(UUID chatId);

    void deleteKey(UUID chatId);
}
