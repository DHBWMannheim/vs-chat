package vs.chat.client;

import vs.chat.entities.Chat;

@FunctionalInterface
public interface OnCreateChat {
    void run(Chat chat);
}
