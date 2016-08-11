package de.domisum.auxiliumapi.data.structure.pds;

import de.domisum.auxiliumapi.AuxiliumAPI;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.entity.Player;

import java.util.HashMap;

@APIUsage
public class PlayerKeyMap<T> extends HashMap<Player, T> implements PlayerDataStructure
{

	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public PlayerKeyMap()
	{
		AuxiliumAPI.getPlayerDataStructureListener().registerPlayerDataStructure(this);
	}


	// -------
	// EVENTS
	// -------
	@APIUsage
	@Override
	public boolean contains(Player player)
	{
		return super.containsKey(player);
	}

	@APIUsage
	@Override
	public boolean removePlayer(Player player)
	{
		return remove(player) != null;
	}

	@Override
	public void onLeave(Player player)
	{

	}

}
