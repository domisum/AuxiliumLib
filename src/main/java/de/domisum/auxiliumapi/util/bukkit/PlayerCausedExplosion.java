package de.domisum.auxiliumapi.util.bukkit;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import de.domisum.auxiliumapi.AuxiliumAPI;
import de.domisum.auxiliumapi.util.java.ThreadUtil;

public class PlayerCausedExplosion implements Listener
{

	// REFERENCES
	protected static PlayerCausedExplosion listener;
	protected static Player currentPlayer;

	protected Player player;

	// PROPERTIES
	protected Location location;

	protected float power = 4;
	protected boolean fire = false;
	protected boolean breakBlocks = true;


	// -------
	// CONSTRUCTOR
	// -------
	public PlayerCausedExplosion(Location location, Player player)
	{
		this.location = location;

		this.player = player;
	}

	protected void registerListener()
	{
		if(listener != null)
			return;

		listener = this;
		JavaPlugin plugin = AuxiliumAPI.getPlugin();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	// -------
	// SETTERS
	// -------
	public PlayerCausedExplosion setPower(float power)
	{
		this.power = power;

		return this;
	}

	public PlayerCausedExplosion setFire(boolean fire)
	{
		this.fire = fire;

		return this;
	}

	public PlayerCausedExplosion setBreakBlocks(boolean breakBlocks)
	{
		this.breakBlocks = breakBlocks;

		return this;
	}


	// -------
	// EXPLOSION
	// -------
	public void detonate()
	{
		ThreadUtil.runSync(() -> detonateSync());
	}

	protected void detonateSync()
	{
		registerListener();
		currentPlayer = this.player;

		this.location.getWorld().createExplosion(this.location.getX(), this.location.getY(), this.location.getZ(), this.power,
				this.fire, this.breakBlocks);

		currentPlayer = null;
	}


	// -------
	// EVENTS
	// -------
	@EventHandler(priority = EventPriority.LOWEST)
	public void entityDeathByExplosion(EntityDeathEvent event)
	{
		// this listener is only active during the explosion, so the damage in here can automatically be attributed to the player

		if(currentPlayer == null)
			return;

		// set killer to player so other listeners pick it up as the player is the killer
		setKiller(event.getEntity());
	}

	protected void setKiller(Entity killed)
	{
		// ((CraftLivingEntity) event.getEntity()).getHandle().killer = ((CraftPlayer) currentPlayer).getHandle();
		// FIXME reflection code for this
	}

}
