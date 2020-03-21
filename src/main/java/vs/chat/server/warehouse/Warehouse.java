package vs.chat.server.warehouse;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import vs.chat.server.persistance.PersistanceHandler;

public class Warehouse {

	private transient static final String SAVE_FILE_NAME = "warehouse";
	// TODO simplify this and improve type safety -> remove casts
	private transient final PersistanceHandler<ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>>> warehousePersistanceHandler = new PersistanceHandler<>(
			SAVE_FILE_NAME);
	private ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> warehouse = new ConcurrentHashMap<>();// TODO
																																	// handle
																																	// nulls
																																	// as
																																	// optionals

	public Warehouse() {
		Stream.of(WarehouseResourceType.values()).forEach(type -> warehouse.put(type, new ConcurrentHashMap<>()));
		
		try {
			this.warehouse = this.warehousePersistanceHandler.load();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("No save found");
//			e.printStackTrace();
		}

		Stream.of(WarehouseResourceType.values())
				.forEach(type -> System.out.println("" + type + warehouse.get(type)));
	}

	public ConcurrentHashMap<WarehouseResourceType, ConcurrentHashMap<UUID, Warehouseable>> get() {
		return this.warehouse;
	}
	
	public ConcurrentHashMap<UUID, Warehouseable> get(final WarehouseResourceType type) {
		return this.get().get(type);
	}

	public void close() throws IOException {
		this.warehousePersistanceHandler.store(warehouse);
	}

}
