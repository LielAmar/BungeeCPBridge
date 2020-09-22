package com.lielamar.completepermissions.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.lielamar.completepermissions.Main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class MySQLManager {
	
	private Connection connection;
	private MySQLPlayerGetterSetter playerGetterSetter = null;
	private MySQLGroupGetterSetter groupGetterSetter = null;
	
	public static final String groupsTable = "groups";
	public static final String groupsParentsTable = "groups_parents";
	public static final String groupsPermissionsTable = "groups_permissions";
	public static final String playersTable = "players";
	public static final String playersGroupsTable = "players_groups";
	public static final String playersPermissionsTable = "players_permissions";
	
	public void setup() {
		String host = Main.getConfig().getConfig().getString("MySQL.host");
		String database = Main.getConfig().getConfig().getString("MySQL.database");
		String username = Main.getConfig().getConfig().getString("MySQL.username");
		String password = Main.getConfig().getConfig().getString("MySQL.password");
		int port = 3306;
		
		if(connect(host, database, username, password, port)) {
			this.playerGetterSetter = new MySQLPlayerGetterSetter(connection);
			this.groupGetterSetter = new MySQLGroupGetterSetter(connection);
		} else
			return;
	}
	
	public boolean connect(String host, String database, String username, String password, int port) {
		try {
			synchronized (this) {
				if ((getConnection() != null) && (!getConnection().isClosed())) {
					return true;
				}
				Class.forName("com.mysql.jdbc.Driver");
				setConnection(DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/"
						+ database + "?createDatabaseIfNotExist=true", username, password));
				setupDatabase();
				Main.getInstance().getProxy().getConsole().sendMessage(new TextComponent(ChatColor.GREEN + "[MySQL] Done setup"));
			}
			return true;
		} catch (SQLException e) {
			return false;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	public Connection getConnection() {
		return this.connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setupDatabase() {
		try {
			String tbl_groups = "CREATE TABLE IF NOT EXISTS `" + groupsTable + "` (`name` varchar(64), `prefix` varchar(32), `suffix` varchar(32), `isdefault` varchar(10));";
			String tbl_groups_permissions = "CREATE TABLE IF NOT EXISTS `" + groupsPermissionsTable + "` (`name` varchar(64), `permission` varchar(64));";
			String tbl_groups_parents = "CREATE TABLE IF NOT EXISTS `" + groupsParentsTable + "` (`name` varchar(64), `parent` varchar(64));";

			String tbl_players = "CREATE TABLE IF NOT EXISTS `" + playersTable + "` (`uuid` varchar(40), `name` varchar(40), `prefix` varchar(32), `suffix` varchar(32));";
			String tbl_players_permissions = "CREATE TABLE IF NOT EXISTS `" + playersPermissionsTable + "` (`uuid` varchar(40), `permission` varchar(64));";
			String tbl_players_groups = "CREATE TABLE IF NOT EXISTS `" + playersGroupsTable + "` (`uuid` varchar(40), `parent` varchar(64));";

			this.connection.prepareStatement(tbl_groups).executeUpdate();
			this.connection.prepareStatement(tbl_groups_permissions).executeUpdate();
			this.connection.prepareStatement(tbl_groups_parents).executeUpdate();

			this.connection.prepareStatement(tbl_players).executeUpdate();
			this.connection.prepareStatement(tbl_players_permissions).executeUpdate();
			this.connection.prepareStatement(tbl_players_groups).executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public MySQLPlayerGetterSetter getStoragePlayerGetterSetter() {
		return this.playerGetterSetter;
	}
	
	public MySQLGroupGetterSetter getStorageGroupGetterSetter() {
		return this.groupGetterSetter;
	}
}