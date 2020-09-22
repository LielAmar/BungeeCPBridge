package com.lielamar.completepermissions.commands;

import com.lielamar.completepermissions.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BungeeCPBridge extends Command {

	public BungeeCPBridge() {
		super("bungeecpbridge", "cp.commands.bungeecpbridge", new String[] { "bcpb" });
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		for(ProxiedPlayer pl : Main.getInstance().getProxy().getPlayers()) {
			for(String permission : pl.getPermissions()) {
				pl.setPermission(permission, false);
			}
			for(String permission : Main.getMySQLManager().getStoragePlayerGetterSetter().getUserPermissions(pl.getUniqueId())) {
				pl.setPermission(permission, true);
			}
		}
		sender.sendMessage(new TextComponent(ChatColor.GREEN + "Synced Bungeecord with CompletePermissions!").toLegacyText());
	}
	
}
