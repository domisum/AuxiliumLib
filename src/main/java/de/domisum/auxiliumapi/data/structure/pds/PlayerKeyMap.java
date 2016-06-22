package de.domisum.auxiliumapi.data.structure.pds;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.domisum.auxiliumapi.AuxiliumAPI;

public class PlayerKeyMap<T> extends HashMap<Player, T> implements PlayerDataStructure
{

	// -------
	// CONSTRUCTOR
	// -------
	public PlayerKeyMap()
	{
		AuxiliumAPI.getPlayerDataStructureListener().registerPlayerDataStructure(this);
	}


	// -------
	// EVENTS
	// -------
	@Override
	public boolean contains(Player player)
	{
		return super.containsKey(player);
	}

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
