package vs.chat.server.persistance;

import java.io.Serializable;

public interface Identifiable extends Serializable {

	public String getIdentifier();
}
