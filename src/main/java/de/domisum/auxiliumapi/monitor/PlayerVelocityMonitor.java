package de.domisum.auxiliumapi.monitor;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerVelocityMonitor
{

	// REFERENCES
	protected Player player;

	// STATUS
	protected boolean terminated = false;

	protected Vector playerVelocity = new Vector(0, 0, 0);
	protected long lastMove;


	// -------
	// CONSTRUCTOR
	// -------
	protected PlayerVelocityMonitor(Player player)
	{
		this.player = player;
	}

	public void terminate()
	{
		this.terminated = true;
	}


	// -------
	// GETTERS
	// -------
	public Player getPlayer()
	{
		return this.player;
	}

	public Vector getVelocity()
	{
		return this.playerVelocity.clone();
	}

	public boolean isTerminated()
	{
		return this.terminated;
	}


	protected long getDurationSinceLastMoveMs()
	{
		return System.currentTimeMillis() - this.lastMove;
	}


	// -------
	// UPDATE
	// -------
	protected void updatePlayerVelocity(Vector velocity)
	{
		this.playerVelocity = velocity;
		this.lastMove = System.currentTimeMillis();
	}

	protected void tick()
	{
		if(getDurationSinceLastMoveMs() > 100)
			this.playerVelocity.multiply(0);
	}

}
