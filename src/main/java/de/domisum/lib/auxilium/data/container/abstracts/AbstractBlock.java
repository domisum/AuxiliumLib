package de.domisum.lib.auxilium.data.container.abstracts;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.SetByDeserialization;
import org.bukkit.Material;

/**
 * Class for serializing and deserializing blocks
 */
@APIUsage
public class AbstractBlock
{

	// PROPERTIES
	@SetByDeserialization
	public final Material material;
	@SetByDeserialization
	public final byte data;


	// -------
	// CONSTRUCTOR
	// -------
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

}
