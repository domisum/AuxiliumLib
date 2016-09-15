package de.domisum.lib.auxilium.util.bukkit;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MessagingUtil
{

	@APIUsage
	public static void sendActionBarMessage(String message, Player... players)
	{
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \""+message+"\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);

		for(Player p : players)
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoc);
	}


	@APIUsage
	public static void sendStatusMessage(Player player, String message)
	{
		player.sendMessage(ChatColor.GRAY.toString()+ChatColor.ITALIC+"["+message+"]");
	}

}
