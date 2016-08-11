package de.domisum.auxiliumapi.util.bukkit;

import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class LocationUtil
{

	@APIUsage
	public static Location lookAt(Location base, Location target)
	{
		double dX = target.getX()-base.getX();
		double dY = target.getY()-base.getY();
		double dZ = target.getZ()-base.getZ();

		double dXZ = Math.sqrt((dX*dX)+(dZ*dZ));

		Location lookingLocation = base.clone();
		// noinspection SuspiciousNameCombination
		lookingLocation.setYaw((float) -Math.toDegrees(Math.atan2(dX, dZ)));
		lookingLocation.setPitch((float) -Math.toDegrees(Math.atan2(dY, dXZ)));

		return lookingLocation;
	}

	@APIUsage
	public static Location getCenter(Block block)
	{
		return block.getLocation().add(0.5, 0.5, 0.5);
	}

	@APIUsage
	public static Location getCenter(Location location)
	{
		return new Location(location.getWorld(), location.getBlockX()+.5, location.getBlockY()+.5, location.getBlockZ()+.5,
				location.getYaw(), location.getPitch());
	}

	@APIUsage
	public static Location getFloorCenter(Location location)
	{
		return getCenter(location).add(0, -.5, 0);
	}


	@APIUsage
	public static Location moveLocationTowardsYaw(Location location, double distance)
	{
		double dX = -Math.sin(Math.toRadians(location.getYaw()))*distance;
		double dZ = Math.cos(Math.toRadians(location.getYaw()))*distance;

		return location.clone().add(dX, 0, dZ);
	}


	// PLAYER
	@APIUsage
	public static Location getPlayerHandLocation(Player player)
	{
		return getPlayerHandLocation(player, false);
	}

	@APIUsage
	public static Location getPlayerHandLocation(Player player, boolean offhand)
	{
		Location handLocation = player.getLocation().clone().add(0, 1.5, 0);
		Vector dir = handLocation.getDirection().setY(0).normalize();
		handLocation.add(dir.multiply(0.4));

		double sideDX = -Math.cos(Math.toRadians(handLocation.getYaw()));
		double sideDZ = -Math.sin(Math.toRadians(handLocation.getYaw()));

		Vector perp = new Vector(sideDX, 0, sideDZ);
		handLocation.add(perp.multiply((offhand ? -1 : 1)*0.4));

		return handLocation;
	}

}
