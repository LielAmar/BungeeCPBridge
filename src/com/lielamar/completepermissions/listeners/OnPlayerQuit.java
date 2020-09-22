package com.lielamar.completepermissions.listeners;

import java.util.LinkedList;
import java.util.List;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnPlayerQuit implements Listener {

	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
        ProxiedPlayer p = e.getPlayer();
        if(p.getPermissions() == null || p.getPermissions().size() == 0) return;
        
        List<String> perms = new LinkedList<String>();
        for(String s : p.getPermissions())
        	perms.add(s);
        
        for(String s : perms)
        	p.setPermission(s, false);
	}
}
