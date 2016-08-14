package de.domisum.auxiliumapi.data.structure.pds;

import de.domisum.auxiliumapi.AuxiliumAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PlayerDataStructureListener implements Listener
{

	// REFERENCES
	private List<WeakReference<PlayerDataStructure>> playerDataStructures = new ArrayList<>();


	// -------
	// CONSTRUCTOR
	// -------
	public PlayerDataStructureListener()
	{
		registerListener();
	}

	private void registerListener()
	{
		Plugin plugin = AuxiliumAPI.getPlugin();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	// -------
	// REGISTRATION
	// -------
	void registerPlayerDataStructure(PlayerDataStructure pds)
	{
		this.playerDataStructures.add(new WeakReference<>(pds));
	}


	// -------
	// EVENTS
	// -------
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerLeave(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		Iterator<WeakReference<PlayerDataStructure>> iterator = this.playerDataStructures.iterator();
		while(iterator.hasNext())
		{
			WeakReference<PlayerDataStructure> ref = iterator.next();
			PlayerDataStructure pds = ref.get();
			if(pds == null)
			{
				iterator.remove();
				continue;
			}

			if(!pds.contains(player))
				continue;

			pds.onLeave(player);
			pds.removePlayer(player);
		}
	}

}
