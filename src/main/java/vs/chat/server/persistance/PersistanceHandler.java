package vs.chat.server.persistance;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistanceHandler<T> {//TODO

	private final String type;

	public PersistanceHandler(final String type) {
		this.type = type;
	}

	public void store(final T object) throws IOException {
		var stream = new FileOutputStream(type + ".dat");
		var outputStream = new ObjectOutputStream(stream);
		outputStream.writeObject(object);
		stream.close();
	}

	@SuppressWarnings("unchecked")
	public T load() throws IOException, ClassNotFoundException {
		var stream = new FileInputStream(type + ".dat");
		var inputStream = new ObjectInputStream(stream);
		var object = inputStream.readObject();
		stream.close();
		return (T) object;
	}
}
