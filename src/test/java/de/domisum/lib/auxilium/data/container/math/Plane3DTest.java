package de.domisum.lib.auxilium.data.container.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Plane3DTest
{

	@Test
	void testInitPointsOnLineFail()
	{
		Vector3D a = new Vector3D(0, 0, 0);
		Vector3D b = new Vector3D(1, 0, 1);
		Vector3D c = new Vector3D(2, 0, 2);

		Assertions.assertThrows(Exception.class, ()->Plane3D.throughPoints(a, b, c));
	}

	@Test
	void testAxisAlignedDistance()
	{
		Vector3D a = new Vector3D(0, 0, 0);
		Vector3D b = new Vector3D(1, 0, 1);
		Vector3D c = new Vector3D(0, 0, 1);
		Plane3D plane = Plane3D.throughPoints(a, b, c);

		Vector3D point = new Vector3D(0, 5, 0);

		Assertions.assertEquals(5.0, plane.distanceTo(point));
	}

}
