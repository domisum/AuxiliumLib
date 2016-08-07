package de.domisum.auxiliumapi.util;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.domisum.auxiliumapi.AuxiliumAPI;

public class DebugUtil
{


	public static void say(Object message)
	{
		say(message + "");
	}

	public static void say(String message)
	{
		String formattedMessage = "[DEBUG] " + message;

		AuxiliumAPI.getPlugin().getLogger().log(Level.WARNING, formattedMessage);
		for(Player p : Bukkit.getOnlinePlayers())
			p.sendMessage(formattedMessage);
	}

}
