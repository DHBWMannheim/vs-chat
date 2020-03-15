package vs.chat.server;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import vs.chat.server.persistance.PersistanceHandler;

public class Warehouse {

	private static final String SAVE_FILE_NAME = "warehouse";
	//TODO simplify this and improve type safety -> remove casts
	private final PersistanceHandler<ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>>> warehousePersistanceHandler = new PersistanceHandler<>(
			SAVE_FILE_NAME);
	private ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> warehouse = new ConcurrentHashMap<>();

	public Warehouse() {
		try {
			this.warehouse = this.warehousePersistanceHandler.load();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ConcurrentHashMap<UUID, Warehouseable> get(final WarehouseResourceType type) {
		return this.warehouse.get(type);
	}

	public void close() throws IOException {
		this.warehousePersistanceHandler.store(warehouse);
	}

}
