package vs.chat.server.warehouse;

import java.io.Serializable;
import java.util.UUID;

public interface Warehouseable extends Serializable {

	public UUID getId();
}
