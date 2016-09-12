package de.domisum.lib.auxilium.data.structure.pds;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.AuxiliumLib;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@APIUsage
public class PlayerList extends ArrayList<Player> implements PlayerDataStructure
{

	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public PlayerList()
	{
		AuxiliumLib.getPlayerDataStructureListener().registerPlayerDataStructure(this);
	}

	@APIUsage
	public PlayerList(Player[] players)
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
