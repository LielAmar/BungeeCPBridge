package com.lielamar.completepermissions;

import com.lielamar.completepermissions.backend.MySQLManager;
import com.lielamar.completepermissions.commands.BungeeCPBridge;
import com.lielamar.completepermissions.listeners.OnPlayerJoin;
import com.lielamar.completepermissions.listeners.OnPlayerQuit;
import com.lielamar.completepermissions.utils.ConfigManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

	private static Main instance;
	private static ConfigManager config;
	private static MySQLManager sqlManager;
	
	@Override
	public void onEnable() {
		
		instance = this;
		config = ConfigManager.getInstance();
		sqlManager = new MySQLManager();
		sqlManager.setup();
		
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new BungeeCPBridge());
		
		getProxy().getPluginManager().registerListener(this, new OnPlayerJoin());
		getProxy().getPluginManager().registerListener(this, new OnPlayerQuit());
	}
	
	@Override
	public void onDisable() {
	}
	
	public static Main getInstance(){ return instance; }
	public static ConfigManager getConfig(){ return config; }
	public static MySQLManager getMySQLManager(){ return sqlManager; }
} 
