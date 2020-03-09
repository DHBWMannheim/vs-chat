package vs.chat.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistanceChatHandler {

	public void store(final Chat chat) throws IOException {
		var stream = new FileOutputStream(chat.getId().toString());
		var outputStream = new ObjectOutputStream(stream);
		outputStream.writeObject(chat);
		stream.close();
	}

	public Chat load(final Integer id) throws IOException, ClassNotFoundException {
		var stream = new FileInputStream(id.toString());
		var inputStream = new ObjectInputStream(stream);
		var chat = inputStream.readObject();
		stream.close();
		return (Chat) chat;
	}
}
