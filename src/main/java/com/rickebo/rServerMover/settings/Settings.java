package com.rickebo.rServerMover.settings;

import java.util.List;

public class Settings
{
	public String rejoinServer = "factions";
	public String fromServer = "lobby";
	public int playersPerSecond = 3;
	public int playerMoveTimeout = 10000;
	public String movePlayerMessage = "Switching server...";
	
	public Settings()
	{
	
	}
	
	public int getPlayerMoveTimeout()
	{
		return playerMoveTimeout;
	}
	
	public void setPlayerMoveTimeout(int playerMoveTimeout)
	{
		this.playerMoveTimeout = playerMoveTimeout;
	}
	
	public String getFromServer()
	{
		return fromServer;
	}
	
	public void setFromServer(String fromServer)
	{
		this.fromServer = fromServer;
	}
	
	public String getMovePlayerMessage()
	{
		return movePlayerMessage;
	}
	
	public void setMovePlayerMessage(String movePlayerMessage)
	{
		this.movePlayerMessage = movePlayerMessage;
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