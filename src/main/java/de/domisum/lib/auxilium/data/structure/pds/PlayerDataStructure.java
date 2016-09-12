package de.domisum.lib.auxilium.data.structure.pds;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.bukkit.entity.Player;

@APIUsage
public interface PlayerDataStructure
{

	boolean contains(Player player);

	boolean removePlayer(Player player);

	void onLeave(Player player);

}
