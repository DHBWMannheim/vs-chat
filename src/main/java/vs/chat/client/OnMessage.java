package vs.chat.client;

import vs.chat.entities.Message;

@FunctionalInterface
public interface OnMessage {
    void run(Message message);
}
