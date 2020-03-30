package vs.chat.client;

import vs.chat.entities.Message;

import java.util.Set;

@FunctionalInterface
public interface OnGetChatMessages {
    void run(Set<Message> messages);
}
