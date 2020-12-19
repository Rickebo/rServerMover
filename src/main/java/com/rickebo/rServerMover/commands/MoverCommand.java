package com.rickebo.rServerMover.commands;

import com.rickebo.rServerMover.G;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class MoverCommand extends Command
{
    public MoverCommand(String command)
    {
        super(command);
    }
    
    @Override
    public void execute(CommandSender commandSender, String[] strings)
    {
        String argument = strings.length > 0 ? strings[0] : null;
        
        if (argument == null)
        {
            handleHelpCommand(commandSender);
            return;
        }
        
        Boolean newState = null;
        
        argument = argument.toLowerCase();
        
        switch (argument)
        {
            case "enable":
            case "enabled":
            case "on":
            case "true":
                newState = true;
                break;
            
            case "disable":
            case "disabled":
            case "off":
            case "false":
                newState = false;
                break;
            
            case "toggle":
                newState = !G.isEnableServerMover();
                break;
                
            case "move":
                movePlayers(commandSender);
                return;
        }
    
        if (!G.hasPermission(commandSender::hasPermission, "rServerMover.Toggle"))
        {
            G.sendMessage(commandSender, "You dont have permission to do that.");
            return;
        }
        
        if (newState == null)
        {
            G.sendMessage(commandSender, "Invalid argument specified.");
            handleHelpCommand(commandSender);
            return;
        }
        
        G.setEnableServerMover(newState);
        G.sendMessage(commandSender, "Server mover has been " + G.settings.highlightColor +
                (newState ? "enabled" : "disabled") + G.settings.textColor + ".");
    }
    
    private void handleHelpCommand(CommandSender sender)
    {
        G.sendMessage(sender, "Command usage: " + G.settings.highlightColor + "/" +
                G.settings.getMoverCommand() + " <move/enable/disable/toggle>");
    }
    
    private void movePlayers(CommandSender sender)
    {
        if (!G.hasPermission(sender::hasPermission, "rServerMover.Move"))
        {
            G.sendMessage(sender, "You dont have permission to do that.");
            return;
        }
        
        G.playerMover.startMoving();
        G.sendMessage(sender, "Moving players...");
    }
}
