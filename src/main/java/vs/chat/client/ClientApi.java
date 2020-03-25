package vs.chat.client;

import vs.chat.entities.Chat;

import java.io.IOException;
import java.util.UUID;

public interface ClientApi {

    public void login(String username, String password) throws IOException, ClassNotFoundException;

    public Chat createChat(String chatName, UUID... userIds) throws IOException, ClassNotFoundException;

    public void sendMessage(String message, UUID target) throws IOException, ClassNotFoundException;

    public void exit() throws IOException;

}