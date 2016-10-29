package de.domisum.lib.auxilium.data.structure.pds;

import de.domisum.lib.auxilium.AuxiliumLib;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.bukkit.entity.Player;

import java.util.HashSet;

@APIUsage
public class PlayerSet extends HashSet<Player> implements PlayerDataStructure
{

	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public PlayerSet()
	{
		AuxiliumLib.getPlayerDataStructureListener().registerPlayerDataStructure(this);
	}

	@APIUsage
	public PlayerSet(Player[] players)
	{
		this();

		for(Player p : players)
			add(p);
	}


	// -------
	// EVENTS
	// -------
	@APIUsage
	@Override
	public boolean contains(Player player)
	{
		return super.contains(player);
	}

	@APIUsage
	@Override
	public boolean removePlayer(Player player)
	{
		return remove(player);
	}

	@Override
	public void onLeave(Player player)
	{

	}

}
