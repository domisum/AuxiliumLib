package de.domisum.auxiliumapi.data.container.abstracts;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import de.domisum.auxiliumapi.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.auxiliumapi.util.java.annotations.SetByDeserialization;
import org.bukkit.Material;

@APIUsage
public class AbstractBlock
{

	// PROPERTIES
	@SetByDeserialization
	public Material material;
	@SetByDeserialization
	public byte data;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public AbstractBlock()
	{

	}

	@APIUsage
	public AbstractBlock(Material material, byte data)
	{
		this.material = material;
		this.data = data;
	}

}
