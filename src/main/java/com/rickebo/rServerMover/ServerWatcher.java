package com.rickebo.rServerMover;

import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.Timer;
import java.util.TimerTask;

public class ServerWatcher
{
	private String serverName;
	private boolean isDown;
	private Timer timer;
	
	private int delay = 1000;
	private int period = 1000;
	
	private Runnable callback;
	private Plugin plugin;
	
	public ServerWatcher(Plugin plugin, String serverName, Runnable callback)
	{
		this.plugin = plugin;
		this.serverName = serverName;
		this.timer = new Timer();
		this.callback = callback;
	}
	
	public void start()
	{
		this.timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				timerElapsed();
			}
		}, delay, period);
	}
	
	private void timerElapsed()
	{
		ServerInfo info = plugin.getProxy().getServerInfo(serverName);
		
		info.ping((ping, throwable) -> checkServer(throwable == null));
	}
	
	private void checkServer(boolean status)
	{
		boolean wasDown = isDown;
		boolean isChange = wasDown && status;
		
		isDown = !status;
		
		if (isChange)
		{
			plugin.getLogger().info("[rServerMover] \"Rejoin-server\" startup detected.");
			new Thread(callback::run).start();
		}
	}
}
