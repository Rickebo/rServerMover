package com.rickebo.rServerMover.settings;

import java.util.List;

public class Settings
{
	public String rejoinServer = "factions";
	public String fromServer = "lobby";
	public int playersPerSecond = 3;
	
	public Settings()
	{
	
	}
	
	public String getRejoinServer()
	{
		return rejoinServer;
	}
	
	public void setRejoinServer(String rejoinServer)
	{
		this.rejoinServer = rejoinServer;
	}
	
	public int getPlayersPerSecond()
	{
		return playersPerSecond;
	}
	
	public void setPlayersPerSecond(int playersPerSecond)
	{
		this.playersPerSecond = playersPerSecond;
	}
}