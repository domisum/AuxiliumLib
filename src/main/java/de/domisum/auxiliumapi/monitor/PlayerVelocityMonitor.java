package de.domisum.auxiliumapi.monitor;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class PlayerVelocityMonitor
{

	// CONSTANTS
	protected static final int TIMEOUT_DURATION_MS = 20*1000;

	// REFERENCES
	protected Player player;

	// STATUS
	protected boolean terminated = false;

	protected Vector playerVelocity = new Vector(0, 0, 0);
	protected long lastTick;
	protected long lastMove;

	protected long lastCheck = System.currentTimeMillis();


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
	public boolean isTooYoung()
	{
		return (this.lastTick == 0) && (this.lastMove == 0);
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Vector getVelocity()
	{
		this.lastCheck = System.currentTimeMillis();

		return this.playerVelocity.clone();
	}

	public boolean isTerminated()
	{
		return this.terminated;
	}

	public boolean isTimedOut()
	{
		return (this.lastCheck+TIMEOUT_DURATION_MS) < System.currentTimeMillis();
	}


	protected long getDurationSinceLastMoveMs()
	{
		return System.currentTimeMillis()-this.lastMove;
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
		this.lastTick = System.currentTimeMillis();

		if(getDurationSinceLastMoveMs() > 100)
			this.playerVelocity.multiply(0);
	}

}
