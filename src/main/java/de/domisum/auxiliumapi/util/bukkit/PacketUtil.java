package de.domisum.auxiliumapi.util.bukkit;

import net.minecraft.server.v1_9_R1.Packet;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PacketUtil
{

	// VALUE CONVERSION
	public static int toPacketDistance(double d)
	{
		return (int) Math.floor(d*32)*128;
	}

	public static byte toPacketAngle(float f)
	{
		return (byte) ((int) ((f*256.0f)/360.0f));
	}


	// SENDING
	public static void sendPacket(Packet<?> packet, Player... players)
	{
		for(Player p : players)
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
	}

	public static void sendPacket(Packet<?> packet, Collection<? extends Player> players)
	{
		sendPacket(packet, players.toArray(new Player[players.size()]));
	}
}
