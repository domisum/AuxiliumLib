package de.domisum.lib.auxilium.data.container.block;

import de.domisum.lib.auxilium.data.container.Duo;
import de.domisum.lib.auxilium.data.container.abstracts.AbstractBlock;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@APIUsage
public class BlockCollection
{

	// CONSTANTS
	private static final AbstractBlock DEFAULT_BLOCK = new AbstractBlock(Material.AIR, (byte) 0);

	// PROPERTIES
	private Map<BlockCoordinate, AbstractBlock> blocks = new HashMap<>();


	// -------
	// CONSTRUCTOR
	// -------
	@APIUsage
	public BlockCollection()
	{

	}

	@APIUsage
	public BlockCollection(Map<BlockCoordinate, AbstractBlock> blocks)
	{
		this.blocks = blocks;
	}


	// -------
	// GETTERS
	// -------
	public Map<BlockCoordinate, AbstractBlock> getBlocks()
	{
		return this.blocks;
	}

	@APIUsage
	public AbstractBlock get(BlockCoordinate coordinate)
	{
		AbstractBlock block = this.blocks.get(coordinate);
		if(block != null)
			return block;

		return DEFAULT_BLOCK;
	}

	@APIUsage
	public boolean isSet(BlockCoordinate coordinate)
	{
		return this.blocks.containsKey(coordinate);
	}


	@APIUsage
	public Duo<BlockCoordinate, BlockCoordinate> getBounds()
	{
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int minZ = Integer.MAX_VALUE;

		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int maxZ = Integer.MIN_VALUE;

		for(Entry<BlockCoordinate, AbstractBlock> entry : this.blocks.entrySet())
		{
			BlockCoordinate coord = entry.getKey();

			if(coord.x < minX)
				minX = coord.x;

			if(coord.y < minY)
				minY = coord.y;

			if(coord.z < minZ)
				minZ = coord.z;


			if(coord.x > maxX)
				maxX = coord.x;

			if(coord.y > maxY)
				maxY = coord.y;

			if(coord.z > maxZ)
				maxZ = coord.z;
		}

		return new Duo<>(new BlockCoordinate(minX, minY, minZ), new BlockCoordinate(maxX, maxY, maxZ));
	}


	// -------
	// SETTERS
	// -------
	@APIUsage
	public void set(BlockCoordinate coordinate, AbstractBlock block)
	{
		this.blocks.put(coordinate, block);
	}


	// -------
	// BUILDING
	// -------
	@SuppressWarnings("deprecation")
	@APIUsage
	public void buildAt(Location location)
	{
		for(Entry<BlockCoordinate, AbstractBlock> entry : this.blocks.entrySet())
		{
			Location loc = location.clone().add(entry.getKey().x, entry.getKey().y, entry.getKey().z);

			Block block = loc.getBlock();
			block.setType(entry.getValue().material);
			block.setData(entry.getValue().data);
		}
	}

}
