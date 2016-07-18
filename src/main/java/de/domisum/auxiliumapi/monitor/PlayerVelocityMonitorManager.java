package de.domisum.auxiliumapi.monitor;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
	protected Map<Player, List<WeakReference<Object>>> playerVelocityMonitorLocks = new HashMap<Player, List<WeakReference<Object>>>();

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
	public static PlayerVelocityMonitor getFor(Player player, Object lock)
	{
		PlayerVelocityMonitorManager pvmm = AuxiliumAPI.getPlayerVelocityMonitorManager();
		if(pvmm.playerVelocityMonitors.containsKey(player))
		{
			pvmm.addLock(player, lock);
			return pvmm.playerVelocityMonitors.get(player);
		}

		PlayerVelocityMonitor pvm = new PlayerVelocityMonitor(player);
		pvmm.playerVelocityMonitors.put(player, pvm);
		pvmm.addLock(player, lock);
		pvmm.startUpdateTask();

		return pvm;
	}

	protected void addLock(Player player, Object lock)
	{
		List<WeakReference<Object>> locks = this.playerVelocityMonitorLocks.get(player);
		if(locks == null)
		{
			locks = new ArrayList<WeakReference<Object>>();
			this.playerVelocityMonitorLocks.put(player, locks);
		}

		WeakReference<Object> wr = new WeakReference<Object>(lock);
		locks.add(wr);
	}

	protected boolean isMonitorUnlocked(Player player)
	{
		List<WeakReference<Object>> locks = this.playerVelocityMonitorLocks.get(player);
		if(locks == null)
			return true;

		Iterator<WeakReference<Object>> iterator = locks.iterator();
		while(iterator.hasNext())
			if(iterator.next().get() == null)
				iterator.remove();

		return locks.size() == 0;
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

			if(!player.isOnline() || pvm.isTerminated() || isMonitorUnlocked(player))
			{
				iterator.remove();
				this.playerVelocityMonitorLocks.remove(player);
			}
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
