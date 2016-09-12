package de.domisum.lib.auxilium.data.container.block;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.data.container.dir.Axis;

@APIUsage
public class BlockRotator
{

	@APIUsage
	public static byte rotateWood(byte data, Axis axis)
	{
		if(axis == Axis.Y)
			return data;
		else if(axis == Axis.X)
			return (byte) (data+4);
		else if(axis == Axis.Z)
			return (byte) (data+8);

		return -1;
	}

}
