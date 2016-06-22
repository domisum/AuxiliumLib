package de.domisum.auxiliumapi.data.structure.pds;

import org.bukkit.entity.Player;

public interface PlayerDataStructure
{

	public boolean contains(Player player);

	public boolean removePlayer(Player player);

	public void onLeave(Player player);

}
