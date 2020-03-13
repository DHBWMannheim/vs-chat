package vs.chat.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import vs.chat.entities.Message;
import vs.chat.entities.User;
import vs.chat.server.persistance.PersistanceHandler;

public class Warehouse {

	private Set<User> users = Collections.synchronizedSet(new TreeSet<>());
	private Set<Message> messages = Collections.synchronizedSet(new TreeSet<>());
	
	private final PersistanceHandler<Set<User>> userPersistanceHandler = new PersistanceHandler<>("Users");
	private final PersistanceHandler<Set<Message>> messagePersistanceHandler = new PersistanceHandler<>("Messages");
	
	public Warehouse() {
		try {
			this.users =  Collections.synchronizedSet(this.userPersistanceHandler.load());
		} catch (ClassNotFoundException | IOException e) {
		}
		try {
			this.messages = Collections.synchronizedSet(this.messagePersistanceHandler.load());
		} catch (ClassNotFoundException | IOException e) {
		}
	}

	public Set<User> getUsers() {
		return users;
	}

	public Set<Message> getMessages() {
		return messages;
	}

	//TODO push
}
