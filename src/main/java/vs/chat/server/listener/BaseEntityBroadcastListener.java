package vs.chat.server.listener;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import vs.chat.entities.Chat;
import vs.chat.entities.Message;
import vs.chat.entities.User;
import vs.chat.packets.BaseEntityBroadcastPacket;
import vs.chat.packets.NoOpPacket;
import vs.chat.server.ConnectionHandler;
import vs.chat.server.ServerContext;
import vs.chat.server.warehouse.WarehouseResourceType;

public class BaseEntityBroadcastListener implements Listener<BaseEntityBroadcastPacket, NoOpPacket> {

	@Override
	public NoOpPacket next(final BaseEntityBroadcastPacket packet, final ServerContext context,
			final ConnectionHandler handler) throws IOException {

		var entity = packet.getBaseEntity();

		var exists = context.getWarehouse().get(entity.getType()).containsKey(entity.getId());
		if (!exists) {
			context.getWarehouse().get(entity.getType()).put(entity.getId(), entity);
			context.getBroadcaster().send(packet);

			var distributionPacket = packet;
			Set<UUID> distributionUser = null;

			if (entity instanceof Chat) {
				var chat = (Chat) entity;
				distributionUser = chat.getUsers();
			} else if (entity instanceof Message) {
				var message = (Message) entity;
				distributionUser = ((Chat) context.getWarehouse().get(WarehouseResourceType.CHATS)
						.get(message.getTarget())).getUsers();
			} else if (entity instanceof User) {
				distributionUser = context.getWarehouse().get(WarehouseResourceType.USERS).keySet();
				distributionPacket = new BaseEntityBroadcastPacket(
						new User(entity.getId(), ((User) entity).getUsername()));
			}

			for (var user : distributionUser) {
				var localConnections = context.getConnectionForUserId(user);
				for (var connection : localConnections) {
					connection.pushTo(distributionPacket);
				}
			}
		}

		return null;
	}

}
