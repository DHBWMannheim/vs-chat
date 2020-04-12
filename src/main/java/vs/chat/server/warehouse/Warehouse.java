package vs.chat.server.warehouse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import vs.chat.packets.NodeSyncPacket;

public class Warehouse {

	private static final String SAVE_FILE_NAME = "warehouse";
	private final String saveFileName;
	private ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> warehouse = new ConcurrentHashMap<>();
	private Set<UUID> packetIds = Collections.synchronizedSet(new TreeSet<>());

	public Warehouse(final String saveFileIdentifier) {
		Stream.of(WarehouseResourceType.values()).forEach(type -> warehouse.put(type, new ConcurrentHashMap<>()));
		this.saveFileName = SAVE_FILE_NAME + saveFileIdentifier;
	}

	public NodeSyncPacket createNodeSyncPacket() {
		var p = new NodeSyncPacket();
		p.packetIds = this.packetIds;
		p.warehouse = this.warehouse;
		return p;
	}

	public ConcurrentHashMap<UUID, Warehouseable> get(final WarehouseResourceType type) {
		return this.warehouse.get(type);
	}
	
	public boolean knowsPacket(final UUID packetId) {
		return this.packetIds.contains(packetId);
	}
	
	public void savePacket(final UUID packetId) {
		this.packetIds.add(packetId);
	}

	@SuppressWarnings("unchecked")
	public synchronized void load() {
		try (var stream = new FileInputStream(this.saveFileName + ".dat")) {
			var inputStream = new ObjectInputStream(stream);
			this.warehouse = (ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>>) inputStream
					.readObject();
			this.packetIds = (Set<UUID>) inputStream.readObject();
			System.out.println("Loaded warehouse:");
			this.print();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Couldn't load save file.");
		}

	}

	public synchronized void save() throws IOException { // Muss dies besser syncronisiert werden?
		File tempFile = File.createTempFile(this.saveFileName, ".tmp");
		try (var stream = new FileOutputStream(tempFile)) {
			var outputStream = new ObjectOutputStream(stream);
			outputStream.writeObject(this.warehouse);
			outputStream.writeObject(this.packetIds);
		}
		Files.move(Paths.get(tempFile.getPath()), Paths.get(new File(this.saveFileName + ".dat").getPath()),
				StandardCopyOption.ATOMIC_MOVE);
	}

	public void print() {
		Stream.of(WarehouseResourceType.values()).forEach(type -> System.out.println("" + type + warehouse.get(type)));
	}

}
