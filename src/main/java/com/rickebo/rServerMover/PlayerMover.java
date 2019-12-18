package com.rickebo.rServerMover;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

public class PlayerMover
{
	private String targetServer;
	private String fromServer;
	private int playersPerSecond;
	
	private Timer moveTimer;
	private Plugin plugin;
	private Random random;
	
	private int exceptionCount = 0;
	
	public PlayerMover(Plugin plugin, String fromServer, String targetServer, int playersPerSecond)
	{
		this.plugin = plugin;
		this.targetServer = targetServer;
		this.fromServer = fromServer;
		this.playersPerSecond = playersPerSecond;
		this.random = new Random();
		this.moveTimer = new Timer();
	}
	
	public void startMoving()
	{
		exceptionCount = 0;
		
		moveTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				try
				{
					movePlayers();
					exceptionCount = 0;
				} catch (Exception ex)
				{
					exceptionCount++;
					
					if (exceptionCount > 5)
					{
						moveTimer.cancel();
						plugin.getLogger().log(Level.SEVERE, "An exception has occurred while moving players: " + ex.getMessage(), ex);
					}
				}
			}
		}, 0, 1000);
	}
	
	private void movePlayers()
	{
		ServerInfo si = plugin.getProxy().getServerInfo(fromServer);
		
		if (si == null)
		{
			moveTimer.cancel();
			return;
		}
		
		Collection<ProxiedPlayer> players = si.getPlayers();
		
		if (players == null || players.isEmpty())
		{
			moveTimer.cancel();
			return;
		}
		
		Collection<ProxiedPlayer> toMove = playersPerSecond <= 0 || playersPerSecond >= players.size()
			? players
			: pickPlayers(players, Math.min(playersPerSecond, players.size()));
		
		if (toMove.isEmpty())
		{
			moveTimer.cancel();
			return;
		}
		
		for (ProxiedPlayer player : toMove)
			player.connect(si);
	}
	
	private List<ProxiedPlayer> pickPlayers(Collection<ProxiedPlayer> players, int count)
	{
		ProxiedPlayer[] arr = players.toArray(new ProxiedPlayer[0]);
		ArrayList<ProxiedPlayer> remaining = new ArrayList<>(Arrays.asList(arr));
		ArrayList<ProxiedPlayer> picked = new ArrayList<>();
		
		for (int i = 0; i < count; i++)
		{
			int playerIndex = random.nextInt(remaining.size());
			picked.add(remaining.remove(playerIndex));
		}
		
		return picked;
	}
}
