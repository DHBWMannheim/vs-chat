package vs.chat.server.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistanceHandler<T extends Identifiable> {

	public void store(final String type, final T chat) throws IOException {
		var stream = new FileOutputStream(type + "_" + chat.getId().toString() + ".dat");
		var outputStream = new ObjectOutputStream(stream);
		outputStream.writeObject(chat);
		stream.close();
	}

	@SuppressWarnings("unchecked")
	public T load(final String type, final Integer id) throws IOException, ClassNotFoundException {
		var stream = new FileInputStream(type + "_" + id.toString() + ".dat");
		var inputStream = new ObjectInputStream(stream);
		var chat = inputStream.readObject();
		stream.close();
		return (T) chat;
	}
}
