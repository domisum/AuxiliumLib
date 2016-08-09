package de.domisum.auxiliumapi.util;

import de.domisum.auxiliumapi.AuxiliumAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class DebugUtil
{


	public static void say(Object message)
	{
		say(message+"");
	}

	public static void say(String message)
	{
		String formattedMessage = "[DEBUG] "+message;

		AuxiliumAPI.getPlugin().getLogger().log(Level.WARNING, formattedMessage);
		for(Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(formattedMessage);
	}

}
