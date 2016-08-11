package de.domisum.auxiliumapi.util.bukkit;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;

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
