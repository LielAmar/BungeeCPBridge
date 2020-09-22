package com.lielamar.completepermissions.listeners;

import java.util.List;

import com.lielamar.completepermissions.Main;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnPlayerJoin implements Listener {

	@EventHandler
	public void onJoinSetupPerms(PostLoginEvent e) {
		ProxiedPlayer p = e.getPlayer();
		
		if(Main.getMySQLManager().getStoragePlayerGetterSetter() == null) return;
		List<String> playerPermissions = Main.getMySQLManager().getStoragePlayerGetterSetter().getUserPermissions(p.getUniqueId());
		
		for(String permission : playerPermissions) {
			if(permission.startsWith("- ")) {
				permission = permission.substring(2);
				p.setPermission(permission, false);
			} else {
				p.setPermission(permission, true);
			}
		}
	}
}
