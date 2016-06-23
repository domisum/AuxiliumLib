package de.domisum.auxiliumapi.data.container.block;

import org.bukkit.Material;

public class AbstractBlock
{

	// PROPERTIES
	public final Material material;
	public final byte data;


	// -------
	// CONSTRUCTOR
	// -------
	public AbstractBlock(Material material, byte data)
	{
		this.material = material;
		this.data = data;
	}

}
