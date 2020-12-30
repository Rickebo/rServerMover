package com.rickebo.rServerMover;

import com.rickebo.rServerMover.commands.MoverCommand;
import com.rickebo.rServerMover.settings.SettingsLoader;
import net.md_5.bungee.api.ProxyServer;
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
			getLogger().severe("An exception has occurred while initializing rServerMover.:" + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public void beginInitialization() throws IOException
	{
		initSettings();
		initEvents();
		initCommands();
	}
	
	public void initCommands()
	{
		MoverCommand command = new MoverCommand(G.settings.getMoverCommand());
		getProxy().getPluginManager().registerCommand(this, command);
	}
	
	public void initSettings() throws IOException
	{
		SettingsLoader loader = new SettingsLoader("plugins", "rServerMover");
		
		G.settings = loader.loadSettings();
	}
	
	public void initEvents()
	{
		G.playerMover = new PlayerMover(G.plugin, G.settings.fromServer, G.settings.rejoinServer,
				G.settings.playersPerSecond, G.settings.movePlayerMessage, G.settings.playerMoveTimeout);
		
		G.watcher = new ServerWatcher(G.plugin, G.settings.rejoinServer, G.settings.remotePort,
				G.playerMover::startMoving);
		
		G.watcher.start();
	}
}
