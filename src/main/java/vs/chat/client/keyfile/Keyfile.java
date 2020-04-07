package vs.chat.client.keyfile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Keyfile {

	private static final String SAVE_FILE_NAME = "keyfile_";
	private ConcurrentHashMap<KeyfileResourceType, ConcurrentHashMap<UUID, Fileable>> keyfile = new ConcurrentHashMap<>();
	private final String saveFileName;

	public Keyfile(final String saveFileIdentifier) {
		Stream.of(KeyfileResourceType.values()).forEach(type -> keyfile.put(type, new ConcurrentHashMap<>()));
		this.saveFileName = SAVE_FILE_NAME + saveFileIdentifier;
	}

	public ConcurrentHashMap<KeyfileResourceType, ConcurrentHashMap<UUID, Fileable>> get() {
		return this.keyfile;
	}

	public ConcurrentHashMap<UUID, Fileable> get(final KeyfileResourceType type) {
		return this.get().get(type);
	}

	@SuppressWarnings("unchecked")
	public void load() throws FileNotFoundException, IOException, ClassNotFoundException {
		try (var stream = new FileInputStream(this.saveFileName + ".dat")) {
			var inputStream = new ObjectInputStream(stream);
			var object = inputStream.readObject();
			this.keyfile = (ConcurrentHashMap<KeyfileResourceType, ConcurrentHashMap<UUID, Fileable>>) object;
			System.out.println("Loaded Keyfile:");
			this.print();
		}

	}

	public void save() throws IOException {
		File tempFile = File.createTempFile(this.saveFileName, ".tmp");

		try (var stream = new FileOutputStream(tempFile)) {
			var outputStream = new ObjectOutputStream(stream);
			outputStream.writeObject(keyfile);
		}

		System.out.println(tempFile.getPath());

		Files.move(Paths.get(tempFile.getPath()), Paths.get(new File(this.saveFileName + ".dat").getPath()),
				StandardCopyOption.ATOMIC_MOVE);
	}

	public void print() {
		Stream.of(KeyfileResourceType.values()).forEach(type -> System.out.println("" + type + keyfile.get(type)));
	}

}
