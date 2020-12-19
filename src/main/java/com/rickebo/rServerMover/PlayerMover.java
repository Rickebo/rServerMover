package com.rickebo.rServerMover;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;
import java.util.logging.Level;

public class PlayerMover
{
	private String targetServer;
	private String fromServer;
	private String moveMessage;
	private int playersPerSecond;
	
	private boolean timerRunning;
	private Timer moveTimer;
	private Plugin plugin;
	private Random random;
	
	private int exceptionCount = 0;
	private int playerMoveTimeout;
	
	private HashMap<ProxiedPlayer, Long> pendingMove = new HashMap<>();
	
	public PlayerMover(Plugin plugin, String fromServer, String targetServer, int playersPerSecond, String moveMessage, int playerMoveTimeout)
	{
		this.playerMoveTimeout = playerMoveTimeout;
		this.moveMessage = moveMessage;
		this.plugin = plugin;
		this.targetServer = targetServer;
		this.fromServer = fromServer;
		this.playersPerSecond = playersPerSecond;
		this.random = new Random();
	}
	
	public void startMoving()
	{
		exceptionCount = 0;
		
		if (timerRunning)
			moveTimer.cancel();
		
		this.moveTimer = new Timer();
		this.timerRunning = true;
		this.moveTimer.schedule(new TimerTask()
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
						timerRunning = false;
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
		ServerInfo target = plugin.getProxy().getServerInfo(targetServer);
		
		if (!G.isEnableServerMover())
			return;
		
		if (si == null)
		{
			plugin.getLogger().severe("[rServerMover] Could not find the server to move players from.");
			
			cancel();
			return;
		}
		
		Collection<ProxiedPlayer> players = si.getPlayers();
		
		if (players == null || players.isEmpty())
		{
			plugin.getLogger().info("[rServerMover] " + (pendingMove.size() != 0
					? "Server is empty, no players left to move."
					: "No players were moved since the server to move from was empty."));
			
			cancel();
			return;
		}
		
		Collection<ProxiedPlayer> toMove = pickPlayers(players, Math.min(playersPerSecond, players.size()));
		
		if (toMove.isEmpty())
		{
			plugin.getLogger().severe("[rServerMover] Could not find server to move players from.");
			
			cancel();
			return;
		}
		
		plugin.getLogger().info("[rServerMover] Moving " + toMove.size() + " player(s)...");
		
		long now = System.currentTimeMillis();
		for (ProxiedPlayer player : toMove)
		{
			pendingMove.put(player, now);
			player.sendMessage(ChatMessageType.SYSTEM, TextComponent.fromLegacyText(ChatColor.GRAY + "[" + ChatColor.GOLD + "MCS" + ChatColor.GRAY + "] " + moveMessage));
			player.connect(target);
		}
	}
	
	private List<ProxiedPlayer> pickPlayers(Collection<ProxiedPlayer> players, int count)
	{
		ProxiedPlayer[] arr = players.toArray(new ProxiedPlayer[0]);
		ArrayList<ProxiedPlayer> remaining = new ArrayList<>(Arrays.asList(arr));
		ArrayList<ProxiedPlayer> picked = new ArrayList<>();
		
		long now = System.currentTimeMillis();
		for (Map.Entry<ProxiedPlayer, Long> entry : pendingMove.entrySet())
		{
			if (now - entry.getValue() > playerMoveTimeout)
				continue;
			
			remaining.remove(entry.getKey());
		}
		
		if (count == 0)
		{
			picked.addAll(remaining);
		}
		else
		{
			for (int i = 0; i < count; i++)
			{
				int playerIndex = random.nextInt(remaining.size());
				picked.add(remaining.remove(playerIndex));
			}
		}
		
		return picked;
	}
	
	private void cancel()
	{
		plugin.getLogger().info("[rServerMover] Player move has finished.");
		
		timerRunning = false;
		moveTimer.cancel();
		exceptionCount = 0;
		pendingMove = new HashMap<>();
	}
}
