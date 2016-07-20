package de.domisum.auxiliumapi.data.container.block;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import de.domisum.auxiliumapi.data.container.Duo;

public class BlockCollection
{

	// CONSTANTS
	private AbstractBlock DEFAULT_BLOCK = new AbstractBlock(Material.AIR, (byte) 0);

	// PROPERTIES
	private Map<BlockCoordinate, AbstractBlock> blocks = new HashMap<>();


	// -------
	// CONSTRUCTOR
	// -------
	public BlockCollection()
	{

	}


	// -------
	// GETTERS
	// -------
	public AbstractBlock get(BlockCoordinate coordinate)
	{
		AbstractBlock block = this.blocks.get(coordinate);
		if(block != null)
			return block;

		return this.DEFAULT_BLOCK;
	}

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

		return new Duo<BlockCoordinate, BlockCoordinate>(new BlockCoordinate(minX, minY, minZ),
				new BlockCoordinate(maxX, maxY, maxZ));
	}


	// -------
	// SETTERS
	// -------
	public void set(BlockCoordinate coordinate, AbstractBlock block)
	{
		this.blocks.put(coordinate, block);
	}


	// -------
	// GENERATION
	// -------
	public BlockCollection smooth(int minimumSurroundingBlocks, Set<Material> materials, int minimumSurroundingBlocksForFilling,
			AbstractBlock blockForFilling)
	{
		boolean debug = false;

		BlockCollection newCollection = new BlockCollection();
		Duo<BlockCoordinate, BlockCoordinate> bounds = getBounds();

		for(int x = bounds.a.x; x <= bounds.b.x; x++)
			for(int y = bounds.a.y; y <= bounds.b.y; y++)
				for(int z = bounds.a.z; z <= bounds.b.z; z++)
				{
					BlockCoordinate coord = new BlockCoordinate(x, y, z);
					AbstractBlock block = get(coord);
					int numberOfSurroundingBlocks = getNumberOfSurroundingBlocks(coord);

					if(numberOfSurroundingBlocks <= minimumSurroundingBlocks)
						if(materials.contains(block.material))
						{
							if(debug)
								block = new AbstractBlock(Material.GLASS, (byte) 0);
							else
								continue;
						}

					if(numberOfSurroundingBlocks >= minimumSurroundingBlocksForFilling)
						if(block.material == Material.AIR)
						{
							if(debug)
								block = new AbstractBlock(Material.GLASS, (byte) 1);
							else
								block = blockForFilling;
						}

					if(block.material == Material.AIR)
						continue;

					newCollection.set(coord, block);
				}

		return newCollection;
	}

	private int getNumberOfSurroundingBlocks(BlockCoordinate blockCoordinate)
	{
		int surroundingBlocks = 0;

		int searchRadius = 1;
		for(int dX = -searchRadius; dX <= searchRadius; dX++)
			for(int dY = -searchRadius; dY <= searchRadius; dY++)
				for(int dZ = -searchRadius; dZ <= searchRadius; dZ++)
				{
					if((dX == 0) && (dY == 0) && (dZ == 0))
						continue;

					AbstractBlock block = get(blockCoordinate.add(dX, dY, dZ));
					if(block.material.isSolid())
						surroundingBlocks += 1;
				}

		// System.out.println("bc: " + blockCoordinate + " surroundingBlocks: " + surroundingBlocks);

		return surroundingBlocks;
	}


	// -------
	// BUILDING
	// -------
	@SuppressWarnings("deprecation")
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
