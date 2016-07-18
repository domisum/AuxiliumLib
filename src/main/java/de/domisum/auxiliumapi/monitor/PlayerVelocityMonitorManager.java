package de.domisum.auxiliumapi.monitor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import de.domisum.auxiliumapi.AuxiliumAPI;

public class PlayerVelocityMonitorManager implements Listener
{

	// REFERENCES
	protected Map<Player, PlayerVelocityMonitor> playerVelocityMonitors = new HashMap<Player, PlayerVelocityMonitor>();

	// STATUS
	protected BukkitTask updateTask;


	// -------
	// CONSTRUCTOR
	// -------
	public PlayerVelocityMonitorManager()
	{
		registerListener();
	}

	protected void registerListener()
	{
		Plugin plugin = AuxiliumAPI.getPlugin();
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}


	// -------
	// REGISTRATION
	// -------
	public static PlayerVelocityMonitor getFor(Player player)
	{
		PlayerVelocityMonitorManager pvmm = AuxiliumAPI.getPlayerVelocityMonitorManager();
		if(pvmm.playerVelocityMonitors.containsKey(player))
			return pvmm.playerVelocityMonitors.get(player);

		PlayerVelocityMonitor pvm = new PlayerVelocityMonitor(player);
		pvmm.playerVelocityMonitors.put(player, pvm);
		pvmm.startUpdateTask();

		return pvm;
	}


	// -------
	// UPDATE
	// -------
	protected void startUpdateTask()
	{
		if(this.updateTask != null)
			return;

		this.updateTask = Bukkit.getScheduler().runTaskTimer(AuxiliumAPI.getPlugin(), () -> update(), 1, 1);
	}

	protected void stopUpdateTask()
	{
		if(this.updateTask == null)
			return;

		this.updateTask.cancel();
		this.updateTask = null;
	}

	protected void update()
	{
		Iterator<PlayerVelocityMonitor> iterator = this.playerVelocityMonitors.values().iterator();
		while(iterator.hasNext())
		{
			PlayerVelocityMonitor pvm = iterator.next();
			Player player = pvm.getPlayer();

			if(!player.isOnline() || pvm.isTerminated() || pvm.isTimedOut())
				iterator.remove();
			else
				pvm.tick();
		}

		System.out.println(this.playerVelocityMonitors.size());

		if(this.playerVelocityMonitors.size() == 0)
			stopUpdateTask();
	}


	// -------
	// EVENTS
	// -------
	@EventHandler
	public void playerMove(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();

		PlayerVelocityMonitor pvm = this.playerVelocityMonitors.get(player);
		if(pvm == null)
			return;

		Vector playerVelocity = event.getTo().toVector().subtract(event.getFrom().toVector());
		pvm.updatePlayerVelocity(playerVelocity);
	}

}
