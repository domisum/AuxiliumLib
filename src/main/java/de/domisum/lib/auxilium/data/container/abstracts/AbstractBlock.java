package de.domisum.lib.auxilium.data.container.abstracts;

import de.domisum.lib.auxilium.data.container.block.BlockCoordinate;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.SetByDeserialization;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Class for serializing and deserializing blocks
 */
@APIUsage
public class AbstractBlock
{

	// PROPERTIES
	@SetByDeserialization public final Material material;
	@SetByDeserialization public final byte data;


	/*
	// INITIALIZATION
	*/
	@DeserializationNoArgsConstructor
	public AbstractBlock()
	{
		this.material = null;
		this.data = 0;
	}

	/**
	 * @param material The material of the block
	 * @param data     the subId of the block
	 */
	@APIUsage
	public AbstractBlock(Material material, byte data)
	{
		this.material = material;
		this.data = data;
	}


	@APIUsage
	public AbstractBlock(World world, BlockCoordinate blockCoordinate)
	{
		this(world.getBlockAt(blockCoordinate.x, blockCoordinate.y, blockCoordinate.z));
	}

	@APIUsage
	public AbstractBlock(World world, int x, int y, int z)
	{
		this(world.getBlockAt(x, y, z));
	}

	@APIUsage
	public AbstractBlock(Block block)
	{
		this.material = block.getType();
		//noinspection deprecation
		this.data = block.getData();
	}

}
