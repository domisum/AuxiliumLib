package de.domisum.auxiliumapi.data.structure.pds;

import java.util.HashMap;

import org.bukkit.entity.Player;

import de.domisum.auxiliumapi.AuxiliumAPI;

public class PlayerValueMap<T> extends HashMap<T, Player> implements PlayerDataStructure
{

	// -------
	// CONSTRUCTOR
	// -------
	public PlayerValueMap()
	{
		AuxiliumAPI.getPlayerDataStructureListener().registerPlayerDataStructure(this);
	}


	// -------
	// EVENTS
	// -------
	@Override
	public boolean contains(Player player)
	{
		return super.containsValue(player);
	}

	@Override
	public boolean removePlayer(Player player)
	{
		return entrySet().removeIf((entry) ->
		{
			return entry.getValue() == player;
		});
	}

	@Override
	public void onLeave(Player player)
	{

	}

}
