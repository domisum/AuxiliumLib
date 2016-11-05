package de.domisum.lib.auxilium.data.container.block.blockcollectionmod;

import de.domisum.lib.auxilium.data.container.abstracts.AbstractBlock;
import de.domisum.lib.auxilium.data.container.block.BlockCollection;
import de.domisum.lib.auxilium.data.container.block.BlockCoordinate;

import java.util.Map;

public class BlockCollectionOffsetter
{

	// INPUT
	private BlockCollection blockCollectionToOffset;
	private BlockCoordinate offset;

	// OUTPUT
	private BlockCollection blockCollectionOffset;


	/*
	// INITIALIZATION
	*/
	public BlockCollectionOffsetter(BlockCollection blockCollection, BlockCoordinate offset)
	{
		this.blockCollectionToOffset = blockCollection;
		this.offset = offset;
	}


	/*
	// GETTERS
	*/
	public BlockCollection getBlockCollectionOffset()
	{
		return this.blockCollectionOffset;
	}


	/*
	// MODIFICATION
	*/
	public void offset()
	{
		this.blockCollectionOffset = new BlockCollection();

		for(Map.Entry<BlockCoordinate, AbstractBlock> entry : this.blockCollectionToOffset.getBlocks().entrySet())
			this.blockCollectionOffset.set(entry.getKey().add(this.offset), entry.getValue());
	}

}
