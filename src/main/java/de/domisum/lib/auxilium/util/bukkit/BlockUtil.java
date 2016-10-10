package de.domisum.lib.auxilium.util.bukkit;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * A utility class to make changes to blocks easier.
 */
@APIUsage
public class BlockUtil
{

	/*
	// SETTING
	*/

	/**
	 * Sets the material and the subId of the block from the argument
	 * to the material and the subId from the arguments.
	 * <p>
	 * This method doesn't trigger a physics update.
	 *
	 * @param block    the block
	 * @param material the material
	 * @param data     the subId
	 * @see #setMaterialAndData(Block, Material, byte, boolean)
	 */
	@APIUsage
	public static void setMaterialAndData(Block block, Material material, byte data)
	{
		setMaterialAndData(block, material, data, false);
	}

	/**
	 * Sets the material and the subId of the block from the argument
	 * to the material and the subId from the arguments.
	 * <p>
	 * The parameter {@code physics} determines whether a physics update should be
	 * triggered on the block after the changes were made to it.
	 *
	 * @param block    the block
	 * @param material the material
	 * @param data     the subId
	 * @param physics  whether a physics update should be triggered
	 */
	@SuppressWarnings("deprecation")
	@APIUsage
	public static void setMaterialAndData(Block block, Material material, byte data, boolean physics)
	{
		block.setTypeIdAndData(material.getId(), data, physics);
	}

}
