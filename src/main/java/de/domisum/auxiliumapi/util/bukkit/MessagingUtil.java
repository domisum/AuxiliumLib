package de.domisum.auxiliumapi.util.bukkit;

import org.bukkit.entity.Player;

public class MessagingUtil
{

	public static void sendActionBarMessage(String message, Player... players)
	{
		// FIXME reflection code for this

		// @formatter:off
			/*

			IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
			PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);

			for(Player p : players)
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);

			*/
			// @formatter:on
	}

}
