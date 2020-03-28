package vs.chat.client;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.entities.User;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface ClientApi {

    // login with username and password, returns user id, user chat & all users
    void login() throws IOException, ClassNotFoundException;

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

    // create chats with chat name and contacts (userIds)
    void createChat(String chatName, final UUID... userIds) throws IOException, ClassNotFoundException;

    // send chat message to user
    void sendMessage(String message, UUID chatId) throws IOException;

    // read incoming messages
    Message waitForNewMessages() throws IOException, ClassNotFoundException;

    // exit program and close connection to server
    void exit() throws IOException;

}
