package vs.chat.server;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistanceHandler<T extends IIdentifiable> {

	//TODO Sowas wie chat.getClass().getSimpleName() + "-" + ?
	public void store(final T chat) throws IOException {
		var stream = new FileOutputStream(chat.getId().toString() + ".dat");
		var outputStream = new ObjectOutputStream(stream);
		outputStream.writeObject(chat);
		stream.close();
	}

	@SuppressWarnings("unchecked")
	public T load(final Integer id) throws IOException, ClassNotFoundException {
		var stream = new FileInputStream(id.toString() + ".dat");
		var inputStream = new ObjectInputStream(stream);
		var chat = inputStream.readObject();
		stream.close();
		return (T) chat;
	}
}
