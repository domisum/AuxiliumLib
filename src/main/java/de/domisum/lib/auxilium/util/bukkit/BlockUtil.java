package de.domisum.lib.auxilium.util.bukkit;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;

@APIUsage
public class BlockUtil
{

	// -------
	// SETTING
	// -------
	@APIUsage
	public static void setMaterialAndData(Block block, Material material, byte data)
	{
		setMaterialAndData(block, material, data, false);
	}

	@SuppressWarnings("deprecation")
	@APIUsage
	public static void setMaterialAndData(Block block, Material material, byte data, boolean physics)
	{
		block.setTypeIdAndData(material.getId(), data, physics);
	}

}
