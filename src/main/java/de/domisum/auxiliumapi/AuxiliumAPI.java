package de.domisum.auxiliumapi;

import de.domisum.auxiliumapi.data.structure.pds.PlayerDataStructureListener;
import de.domisum.auxiliumapi.monitor.PlayerVelocityMonitorManager;
import de.domisum.auxiliumapi.util.java.annotations.APIUsage;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

public class AuxiliumAPI
{

	// REFERENCES
	private static AuxiliumAPI instance;
	private Plugin plugin;

	// MANAGERS
	private PlayerDataStructureListener playerDataStructureListener;
	private PlayerVelocityMonitorManager playerVelocityMonitorManager;


	// -------
	// CONSTRUCTOR
	// -------
	private AuxiliumAPI(Plugin plugin)
	{
		instance = this;
		this.plugin = plugin;

		onEnable();
	}

	@APIUsage
	public static void enable(Plugin plugin)
	{
		if(instance != null)
			return;

		new AuxiliumAPI(plugin);
	}

	@APIUsage
	public static void disable()
	{
		if(instance == null)
			return;

		getInstance().onDisable();
		instance = null;
	}

	private void onEnable()
	{
		this.playerDataStructureListener = new PlayerDataStructureListener();
		this.playerVelocityMonitorManager = new PlayerVelocityMonitorManager();

		getLogger().info(this.getClass().getSimpleName()+" has been enabled");
	}

	private void onDisable()
	{
		getLogger().info(this.getClass().getSimpleName()+" has been disabled");
	}


	// -------
	// GETTERS
	// -------
	@APIUsage
	public static AuxiliumAPI getInstance()
	{
		if(instance == null)
			throw new IllegalArgumentException(AuxiliumAPI.class.getSimpleName()+" has to be initialized before usage");

		return instance;
	}

	public static Plugin getPlugin()
	{
		return getInstance().plugin;
	}

	public Logger getLogger()
	{
		return getInstance().plugin.getLogger();
	}


	public static PlayerDataStructureListener getPlayerDataStructureListener()
	{
		return getInstance().playerDataStructureListener;
	}

	public static PlayerVelocityMonitorManager getPlayerVelocityMonitorManager()
	{
		return getInstance().playerVelocityMonitorManager;
	}

}
