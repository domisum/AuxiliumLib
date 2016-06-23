package de.domisum.auxiliumapi.util.bukkit;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockUtil
{

	// -------
	// SETTING
	// -------
	public static void setMaterialAndData(Block block, Material material, byte data)
	{
		setMaterialAndData(block, material, data, false);
	}

	@SuppressWarnings("deprecation")
	public static void setMaterialAndData(Block block, Material material, byte data, boolean physics)
	{
		block.setTypeIdAndData(material.getId(), data, false);
	}

}
