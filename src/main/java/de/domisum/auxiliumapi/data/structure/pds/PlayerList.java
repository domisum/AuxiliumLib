package de.domisum.auxiliumapi.data.structure.pds;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import de.domisum.auxiliumapi.AuxiliumAPI;

public class PlayerList extends ArrayList<Player> implements PlayerDataStructure
{

	// -------
	// CONSTRUCTOR
	// -------
	public PlayerList()
	{
		AuxiliumAPI.getPlayerDataStructureListener().registerPlayerDataStructure(this);
	}

	public PlayerList(Player[] players)
	{
		this();

		for(Player p : players)
			add(p);
	}


	// -------
	// EVENTS
	// -------
	@Override
	public boolean contains(Player player)
	{
		return super.contains(player);
	}

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
