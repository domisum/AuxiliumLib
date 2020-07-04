package io.domisum.lib.auxiliumlib.datacontainers.math;

import io.domisum.lib.auxiliumlib.annotations.API;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@API
@RequiredArgsConstructor
@ToString
public class Quaternion
{
	
	// ATTRIBUTES
	@Getter
	private final double w;
	@Getter
	private final double x;
	@Getter
	private final double y;
	@Getter
	private final double z;
	
	
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
		x = sin*vector.getX();
		y = sin*vector.getY();
		z = sin*vector.getZ();
	}
	
	@API
	public static Quaternion getRotationFromTo(Vector3D vector1, Vector3D vector2)
	{
		double norm = Math.sqrt(vector1.lengthSquared()*vector2.lengthSquared());
		double w = norm+vector1.dotProduct(vector2);
		
		Vector3D axis;
		if(w < (1.e-5d*norm))
		{
			w = 0;
			axis = (vector1.getX() > vector1.getZ()) ?
					new Vector3D(-vector1.getY(), vector1.getX(), 0) :
					new Vector3D(0, -vector1.getZ(), vector1.getY());
		}
		else
			axis = vector1.deriveCrossProduct(vector2);
		
		return new Quaternion(-w, axis.getX(), axis.getY(), axis.getZ()).deriveNormalized(); // idfk why -w, but whatever
	}
	
	
	// SELF
	@API
	public double length()
	{
		return Math.sqrt((w*w)+(x*x)+(y*y)+(z*z));
	}
	
	@API
	public Quaternion deriveNormalized()
	{
		return deriveMultiply(1/length());
	}
	
	@API
	public Quaternion deriveInverse()
	{
		double d = (w*w)+(x*x)+(y*y)+(z*z);
		return new Quaternion(w/d, -x/d, -y/d, -z/d);
	}
	
	@API
	public Quaternion deriveConjugated()
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
	public Quaternion deriveAdd(Quaternion b)
	{
		return new Quaternion(w+b.w, x+b.x, y+b.y, z+b.z);
	}
	
	@API
	public Quaternion deriveMultiply(Quaternion b)
	{
		double nW = (w*b.w)-(x*b.x)-(y*b.y)-(z*b.z);
		double nX = ((w*b.x)+(x*b.w)+(y*b.z))-(z*b.y);
		double nY = ((w*b.y)-(x*b.z))+(y*b.w)+(z*b.x);
		double nZ = (((w*b.z)+(x*b.y))-(y*b.x))+(z*b.w);
		return new Quaternion(nW, nX, nY, nZ);
	}
	
	@API
	public Quaternion deriveMultiply(double d)
	{
		return new Quaternion(w*d, x*d, y*d, z*d);
	}
	
	@API
	public Quaternion deriveDivide(Quaternion b)
	{
		return deriveMultiply(b.deriveInverse());
	}
	
}
