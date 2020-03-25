package vs.chat.server.warehouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Warehouse {

	private static final String SAVE_FILE_NAME = "warehouse";
	private ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> warehouse = new ConcurrentHashMap<>();// TODO
																																	// handle
																																	// nulls
																																	// as
																																	// optionals

	public Warehouse() {
		Stream.of(WarehouseResourceType.values()).forEach(type -> warehouse.put(type, new ConcurrentHashMap<>()));
	}

	public ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> get() {
		return this.warehouse;
	}

	public ConcurrentHashMap<UUID, Warehouseable> get(final WarehouseResourceType type) {
		return this.get().get(type);
	}

	@SuppressWarnings("unchecked")
	public void load() throws FileNotFoundException, IOException, ClassNotFoundException {
		try (var stream = new FileInputStream(SAVE_FILE_NAME + ".dat")) {
			var inputStream = new ObjectInputStream(stream);
			var object = inputStream.readObject();
			this.warehouse = (ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>>) object;
			System.out.println("Loaded warehouse:");
			this.print();
		}

	}

	public void save() throws IOException {
		File tempFile = File.createTempFile(SAVE_FILE_NAME, ".tmp");

		try (var stream = new FileOutputStream(tempFile)) {
			var outputStream = new ObjectOutputStream(stream);
			outputStream.writeObject(warehouse);
		}

		System.out.println(tempFile.getPath());

		Files.move(Paths.get(tempFile.getPath()), Paths.get(new File(SAVE_FILE_NAME + ".dat").getPath()),
				StandardCopyOption.ATOMIC_MOVE);
	}

	public void print() {
		Stream.of(WarehouseResourceType.values()).forEach(type -> System.out.println("" + type + warehouse.get(type)));
	}

}
