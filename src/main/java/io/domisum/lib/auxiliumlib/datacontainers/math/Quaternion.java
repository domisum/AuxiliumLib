package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import io.domisum.lib.auxiliumlib.util.math.MathUtil;
import lombok.AllArgsConstructor;

@API
@AllArgsConstructor
public class Quaternion
{

	// PROPERTIES
	@API
	public final double w;
	@API
	public final double x;
	@API
	public final double y;
	@API
	public final double z;


	// INIT
	@API
	public Quaternion()
	{
		this(0, new Vector3D(1, 0, 0)); // vector can't be null-vector since this fucks everything up
	}

	@API
	public Quaternion(double angleRad, Vector3D vector)
	{
		w = Math.cos(angleRad/2);

		double sin = Math.sin(angleRad/2);
		x = sin*vector.x;
		y = sin*vector.y;
		z = sin*vector.z;
	}

	@Override
	public String toString()
	{
		return "quaternion[w="+MathUtil.round(w, 3)+",x="+MathUtil.round(x, 3)+",y="+MathUtil.round(y, 3)+",z="+MathUtil.round(z,
				3
		)+"]";
	}


	@API
	public static Quaternion getRotationFromTo(Vector3D vector1, Vector3D vector2)
	{
		double norm = Math.sqrt(vector1.lengthSquared()*vector2.lengthSquared());
		double w = norm+vector1.dotProduct(vector2);

		Vector3D axis;
		if(w<(1.e-5d*norm))
		{
			w = 0;
			// noinspection SuspiciousNameCombination
			axis = (vector1.x>vector1.z) ?
					new Vector3D(-vector1.y, vector1.x, 0) :
					new Vector3D(0, -vector1.z, vector1.y);
		}
		else
			axis = vector1.crossProduct(vector2);

		return new Quaternion(-w, axis.x, axis.y, axis.z).normalize(); // idfk why -w, but whatever
	}


	// SELF
	@API
	public double length()
	{
		return Math.sqrt((w*w)+(x*x)+(y*y)+(z*z));
	}

	@API
	public Quaternion normalize()
	{
		return multiply(1/length());
	}

	@API
	public Quaternion inverse()
	{
		double d = (w*w)+(x*x)+(y*y)+(z*z);
		return new Quaternion(w/d, -x/d, -y/d, -z/d);
	}

	@API
	public Quaternion conjugate()
	{
		return new Quaternion(w, -x, -y, -z);
	}

	@API
	public Vector3D getVector()
	{
		return new Vector3D(x, y, z);
	}


	// COMBINE
	@API
	public Quaternion add(Quaternion b)
	{
		return new Quaternion(w+b.w, x+b.x, y+b.y, z+b.z);
	}

	@API
	public Quaternion multiply(Quaternion b)
	{
		double nW = (w*b.w)-(x*b.x)-(y*b.y)-(z*b.z);
		double nX = ((w*b.x)+(x*b.w)+(y*b.z))-(z*b.y);
		double nY = ((w*b.y)-(x*b.z))+(y*b.w)+(z*b.x);
		double nZ = (((w*b.z)+(x*b.y))-(y*b.x))+(z*b.w);
		return new Quaternion(nW, nX, nY, nZ);
	}

	@API
	public Quaternion multiply(double d)
	{
		return new Quaternion(w*d, x*d, y*d, z*d);
	}

	@API
	public Quaternion divide(Quaternion b)
	{
		return multiply(b.inverse());
	}

}
