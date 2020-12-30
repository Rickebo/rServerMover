package com.rickebo.rServerMover;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
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
    
    private final int remotePort;
    
    public ServerWatcher(Plugin plugin, String serverName, int remotePort, Runnable callback)
    {
        this.plugin = plugin;
        this.serverName = serverName;
        this.timer = new Timer();
        this.callback = callback;
        
        this.remotePort = remotePort;
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
     
    	if (remotePort <= 0)
	    {
		    ServerInfo info = plugin.getProxy().getServerInfo(serverName);
		    info.ping((ping, throwable) -> checkServer(throwable == null));
	    }
    	else
	    {
	    	boolean isUp = attemptConnection(remotePort);
	    	
	    	checkServer(isUp);
	    }
    }
    
    private boolean attemptConnection(int port)
    {
	    ServerInfo info = plugin.getProxy().getServerInfo(serverName);
        SocketAddress socketAddress = info.getSocketAddress();
        
        if (!(socketAddress instanceof InetSocketAddress))
            return false;
        
        InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
        InetSocketAddress targetAddress = new InetSocketAddress(
                inetSocketAddress.getAddress(), port);
        
        Socket socket = new Socket();
        
        try
        {
            socket.connect(targetAddress, 10000);
            socket.close();
            return true;
            
        } catch (IOException ex)
        {
            return false;
        }
    }
    
    private void checkServer(boolean status)
    {
        boolean wasDown = isDown;
        boolean isChange = wasDown && status;
        
        isDown = !status;
        
        if (isChange)
        {
            plugin.getLogger().info("[rServerMover] \"Rejoin-server\" startup detected.");
            
            if (!G.isEnableServerMover())
            {
                plugin.getLogger().info("[rServerMover] Ignoring startup since server mover has been disabled.");
                return;
            }
            
            new Thread(callback::run).start();
        }
    }
}
