package de.domisum.auxiliumapi.data.container.block;

import de.domisum.auxiliumapi.data.container.dir.Axis;

public class BlockRotator
{

	public static byte rotateWood(byte data, Axis axis)
	{
		if(axis == Axis.Y)
			return data;
		else if(axis == Axis.X)
			return (byte) (data + 4);
		else if(axis == Axis.Z)
			return (byte) (data + 8);

		return -1;
	}

}
