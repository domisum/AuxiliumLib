package de.domisum.lib.auxilium.data.structure.pds;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.AuxiliumLib;
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
		AuxiliumLib.getPlayerDataStructureListener().registerPlayerDataStructure(this);
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
