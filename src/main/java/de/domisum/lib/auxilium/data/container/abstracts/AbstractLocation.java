package de.domisum.lib.auxilium.data.container.abstracts;

import de.domisum.lib.auxilium.data.container.math.Vector3D;
import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import de.domisum.lib.auxilium.util.java.annotations.DeserializationNoArgsConstructor;
import de.domisum.lib.auxilium.util.java.annotations.SetByDeserialization;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@APIUsage
public class AbstractLocation
{

	// GAME
	@SetByDeserialization
	private String world;

	@SetByDeserialization
	private double x;
	@SetByDeserialization
	private double y;
	@SetByDeserialization
	private double z;

	@SetByDeserialization
	private float yaw;
	@SetByDeserialization
	private float pitch;


	// -------
	// CONSTRUCTOR
	// -------
	@DeserializationNoArgsConstructor
	public AbstractLocation()
	{

	}

	@APIUsage
	public AbstractLocation(String world, double x, double y, double z)
	{
		this(world, x, y, z, 0, 0);
	}

	@APIUsage
	public AbstractLocation(String world, double x, double y, double z, float yaw, float pitch)
	{
		this.world = world;

		this.x = x;
		this.y = y;
		this.z = z;

		this.yaw = yaw;
		this.pitch = pitch;
	}

	@APIUsage
	public AbstractLocation(Location model)
	{
		this.world = model.getWorld().getName();

		this.x = model.getX();
		this.y = model.getY();
		this.z = model.getZ();

		this.yaw = model.getYaw();
		this.pitch = model.getPitch();
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public Location get()
	{
		World bukkitWorld = Bukkit.getWorld(this.world);
		if(bukkitWorld == null)
			throw new IllegalStateException("The world '"+this.world+"' of the abstract location is not loaded");

		return new Location(bukkitWorld, this.x, this.y, this.z, this.yaw, this.pitch);
	}

	@APIUsage
	public Vector3D getPosition()
	{
		return new Vector3D(this.x, this.y, this.z);
	}

}
