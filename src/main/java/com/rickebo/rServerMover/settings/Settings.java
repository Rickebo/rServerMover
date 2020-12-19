package com.rickebo.rServerMover.settings;

import java.util.List;

public class Settings
{
	public String rejoinServer = "factions";
	public String fromServer = "lobby";
	public int playersPerSecond = 3;
	public int playerMoveTimeout = 10000;
	public String movePlayerMessage = "Switching server...";
	public String messagePrefix = "§7[§6MCS§7] ";
	
	public String highlightColor = "§6";
	public String textColor = "§7";
	
	public String moverCommand = "servermover";
	
	public Settings()
	{
	
	}
	
	public String getMoverCommand()
	{
		return moverCommand;
	}
	
	public void setMoverCommand(String moverCommand)
	{
		this.moverCommand = moverCommand;
	}
	
	public String getHighlightColor()
	{
		return highlightColor;
	}
	
	public void setHighlightColor(String highlightColor)
	{
		this.highlightColor = highlightColor;
	}
	
	public String getTextColor()
	{
		return textColor;
	}
	
	public void setTextColor(String textColor)
	{
		this.textColor = textColor;
	}
	
	public String getMessagePrefix()
	{
		return messagePrefix;
	}
	
	public void setMessagePrefix(String messagePrefix)
	{
		this.messagePrefix = messagePrefix;
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