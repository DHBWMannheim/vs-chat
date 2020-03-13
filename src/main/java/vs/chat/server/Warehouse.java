package vs.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import vs.chat.entities.Message;
import vs.chat.entities.User;
import vs.chat.server.persistance.PersistanceHandler;

public class Warehouse {

	private List<User> users = Collections.synchronizedList(new ArrayList<>());
	private List<Message> messages = Collections.synchronizedList(new ArrayList<>());
	
	private final PersistanceHandler<List<User>> userPersistanceHandler = new PersistanceHandler<>("Users");
	private final PersistanceHandler<List<Message>> messagePersistanceHandler = new PersistanceHandler<>("Messages");
	
	public Warehouse() {
		try {
			this.users = Collections.synchronizedList(this.userPersistanceHandler.load());
		} catch (ClassNotFoundException | IOException e) {
		}
		try {
			this.messages = Collections.synchronizedList(this.messagePersistanceHandler.load());
		} catch (ClassNotFoundException | IOException e) {
		}
	}

	public List<User> getUsers() {
		return users;
	}

	public List<Message> getMessages() {
		return messages;
	}

	//TODO push
}
