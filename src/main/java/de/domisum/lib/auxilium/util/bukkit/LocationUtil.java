package de.domisum.lib.auxilium.util.bukkit;

import de.domisum.lib.auxilium.util.java.annotations.APIUsage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * Class with helper methods to make Bukkit {@code Loction} handling easier
 */
@APIUsage
public class LocationUtil
{

	/**
	 * Returns a location with the coordinates of the {@code base} Location
	 * and the yaw and pitch modified in such a way that a character would be
	 * looking at the {@code target} Location.
	 * <p>
	 * If this is used for players, make sure to use the {@code #getEyeLocation()} Location
	 * as the {@code base}, otherwise the player will look up too far.
	 *
	 * @param base   the base location
	 * @param target the location to look at
	 * @return the copy of base that "looks" towards target
	 */
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

	/**
	 * Determines the center Location of a block.
	 * <p>
	 * The center location is determined by taking the coordinates
	 * of each block coordinate and adding {@code 0.5} to each component.
	 *
	 * @param block the block
	 * @return the center location
	 * @see #getCenter(Location)
	 */
	@APIUsage
	public static Location getCenter(Block block)
	{
		return block.getLocation().add(0.5, 0.5, 0.5);
	}

	/**
	 * Determines the center location of the block at the parameter Location.
	 * <p>
	 * Very similar to {@link #getCenter(Block)}
	 *
	 * @param location the location of the block
	 * @return the center location
	 * @see #getCenter(Block)
	 */
	@APIUsage
	public static Location getCenter(Location location)
	{
		return new Location(location.getWorld(), location.getBlockX()+.5, location.getBlockY()+.5, location.getBlockZ()+.5,
				location.getYaw(), location.getPitch());
	}

	/**
	 * Determines the floor center location of the block at the parameter Location.
	 * <p>
	 * The floor center Location is determined very similarly to the center Location.
	 * The only difference is that the y-component of the block is not modified,
	 * leaving it at an integer value. This means the Location determined by this method is on the ground.
	 * <p>
	 * The parameter Location remains unchagned.
	 *
	 * @param location the location of the Block
	 * @return the floor location
	 * @see #getCenter(Location)
	 */
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


	// CHUNK
	@APIUsage
	public static boolean isChunkLoaded(Location location)
	{
		int chunkX = location.getBlockX()>>4;
		int chunkZ = location.getBlockZ()>>4;

		return location.getWorld().isChunkLoaded(chunkX, chunkZ);
	}

}
