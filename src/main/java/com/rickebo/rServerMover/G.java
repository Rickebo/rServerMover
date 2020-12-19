package com.rickebo.rServerMover;

import com.rickebo.rServerMover.settings.Settings;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.function.Function;

public class G
{
	public static Settings settings;
	public static Plugin plugin;
	public static ServerWatcher watcher;
	public static PlayerMover playerMover;
	
	private static boolean enableServerMover = true;
	
	public static boolean hasPermission(Function<String, Boolean> permissionChecker, String permission)
	{
		String[] checks = new String[]{
				permission,
				permission.toUpperCase(),
				permission.toLowerCase()
		};
		
		for (String perm : checks)
		{
			if (permissionChecker.apply(perm))
				return true;
		}
		
		return false;
	}
	
	public static boolean isEnableServerMover()
	{
		return enableServerMover;
	}
	
	public static void setEnableServerMover(boolean enableServerMover)
	{
		G.enableServerMover = enableServerMover;
	}
	
	public static String formatText(String input)
	{
		String chatColorCharacters = ChatColor.ALL_CODES;
		char chatColorPrefix = ChatColor.COLOR_CHAR;
		
		for (char color : chatColorCharacters.toCharArray())
		{
			ChatColor chatColor = ChatColor.getByChar(color);
			
			if (chatColor == null)
				continue;
			
			String formatter = "" + chatColorPrefix + color;
			
			input = input.replaceAll(formatter, chatColor.toString());
		}
		
		return input;
	}
	
	public static void sendMessage(CommandSender sender, String message)
	{
		message = formatText(settings.messagePrefix + message);
		
		sender.sendMessage(new TextComponent(message));
	}
}
