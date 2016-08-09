package de.domisum.auxiliumapi.util.bukkit;

import de.domisum.auxiliumapi.AuxiliumAPI;
import de.domisum.auxiliumapi.util.java.ThreadUtil;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerCausedExplosion implements Listener
{

	// REFERENCES
	protected static PlayerCausedExplosion listener;
	protected static Player currentPlayer;

	protected Player player;

	// PROPERTIES
	protected Location location;

	protected double power = 4;
	protected boolean fire = false;
	protected boolean breakBlocks = true;

	protected boolean damageSelf = true;


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
	public PlayerCausedExplosion setPower(double power)
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

	public PlayerCausedExplosion setDamageSelf(boolean damageSelf)
	{
		this.damageSelf = damageSelf;

		return this;
	}


	// -------
	// EXPLOSION
	// -------
	public void detonate()
	{
		ThreadUtil.runSync(()->detonateSync());
	}

	protected void detonateSync()
	{
		registerListener();
		currentPlayer = this.player;

		this.location.getWorld()
				.createExplosion(this.location.getX(), this.location.getY(), this.location.getZ(), (float) this.power, this.fire,
						this.breakBlocks);

		currentPlayer = null;
	}


	// -------
	// EVENTS
	// -------
	@EventHandler(priority = EventPriority.LOWEST) public void entityDeathByExplosion(EntityDeathEvent event)
	{
		// this listener is only active during the explosion, so the damage in here can automatically be attributed to the player
		// and can only be caused by the explosion

		if(currentPlayer == null)
			return;

		// set killer to player so other listeners pick it up as the player is the killer
		((CraftLivingEntity) event.getEntity()).getHandle().killer = ((CraftPlayer) currentPlayer).getHandle();
	}

	@EventHandler(priority = EventPriority.LOWEST) public void entityDamageByExplosion(EntityDamageEvent event)
	{
		// this listener is only active during the explosion, so the damage in here can automatically be attributed to the player
		// and can only be caused by the explosion

		if(currentPlayer == null)
			return;

		// prevent self damage if disabled
		if(!this.damageSelf)
			if(event.getEntity() == currentPlayer)
				event.setCancelled(true);
	}

}
