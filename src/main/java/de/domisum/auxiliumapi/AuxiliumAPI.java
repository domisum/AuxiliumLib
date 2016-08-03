package de.domisum.auxiliumapi;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import de.domisum.auxiliumapi.data.structure.pds.PlayerDataStructureListener;
import de.domisum.auxiliumapi.monitor.PlayerVelocityMonitorManager;

public class AuxiliumAPI
{

	// REFERENCES
	private static AuxiliumAPI instance;
	private JavaPlugin plugin;

	// MANAGERS
	private PlayerDataStructureListener playerDataStructureListener;
	private PlayerVelocityMonitorManager playerVelocityMonitorManager;


	// -------
	// CONSTRUCTOR
	// -------
	protected AuxiliumAPI(JavaPlugin plugin)
	{
		instance = this;
		this.plugin = plugin;

		onEnable();
	}

	public static void initialize(JavaPlugin plugin)
	{
		new AuxiliumAPI(plugin);
	}

	protected void onEnable()
	{
		this.playerDataStructureListener = new PlayerDataStructureListener();
		this.playerVelocityMonitorManager = new PlayerVelocityMonitorManager();

		getLogger().info(this.getClass().getSimpleName() + " has been enabled");
	}

	public void onDisable()
	{
		instance = null;
		getLogger().info(this.getClass().getSimpleName() + " has been disabled");
	}


	// -------
	// GETTERS
	// -------
	public static boolean isEnabled()
	{
		return instance != null;
	}

	public static AuxiliumAPI getInstance()
	{
		if(instance == null)
			throw new IllegalArgumentException(
					"An instance of AuxiliumAPI has to be constructed before usage of the advanced classes.");

		return instance;
	}

	public static JavaPlugin getPlugin()
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
