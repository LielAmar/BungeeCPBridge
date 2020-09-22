package com.lielamar.completepermissions.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.ByteStreams;
import com.lielamar.completepermissions.Main;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class ConfigManager {

	private static ConfigManager instance = new ConfigManager();
	public static ConfigManager getInstance() { return instance; }
	public ConfigManager() {}
	
	
	private File file = new File(Main.getInstance().getDataFolder(), "config.yml");
	
	public boolean configExists() {
		if(!Main.getInstance().getDataFolder().exists()) {
			Main.getInstance().getDataFolder().mkdir();
		}
        
        return file.exists();
	}
	
	public void saveDefaultConfig() {
        if(!configExists()) {
        	try {
                file.createNewFile();
                try(InputStream is = Main.getInstance().getResourceAsStream("config.yml"); OutputStream os = new FileOutputStream(file)) {
                    ByteStreams.copy(is, os);
                }
            } catch(IOException e) {
                throw new RuntimeException("config.yml copy failed", e);
            }
        }
	}
	
	public void saveConfig(Configuration config) {
		if(!configExists()) saveDefaultConfig();
		
		try {
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(Main.getInstance().getDataFolder(), "config.yml"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Configuration getConfig() {
		if(!configExists()) saveDefaultConfig();
		
		try {
			saveDefaultConfig();
			return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(Main.getInstance().getDataFolder(), "config.yml"));
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
