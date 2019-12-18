package com.rickebo.rServerMover;

import com.rickebo.rServerMover.settings.SettingsLoader;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Initializer extends Plugin
{
	
	@Override
	public void onEnable()
	{
		try
		{
			G.plugin = this;
			beginInitialization();
		} catch (IOException ex)
		{
			getLogger().severe("An exception has occurred while initializing rAntiVPN.:" + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public void beginInitialization() throws IOException
	{
		initSettings();
		initEvents();
	}
	
	public void initSettings() throws IOException
	{
		SettingsLoader loader = new SettingsLoader("plugins", "rServerMover");
		
		G.settings = loader.loadSettings();
	}
	
	public void initEvents()
	{
		G.playerMover = new PlayerMover(G.plugin, G.settings.fromServer, G.settings.rejoinServer, G.settings.playersPerSecond);
		G.watcher = new ServerWatcher(G.plugin, G.settings.rejoinServer, G.playerMover::startMoving);
		
		G.watcher.start();
	}
}
