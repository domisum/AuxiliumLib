package de.domisum.auxiliumapi.data.structure.pds;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.entity.Player;

@APIUsage
public interface PlayerDataStructure
{

	boolean contains(Player player);

	boolean removePlayer(Player player);

	void onLeave(Player player);

}
