package com.lielamar.completepermissions.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lielamar.completepermissions.Main;

public class MySQLPlayerGetterSetter {

	private Connection connection;
	public MySQLPlayerGetterSetter(Connection connection) { this.connection = connection; }
	public Connection getConnection() { return this.connection; }
	
	public List<String> getUserPermissions(UUID u) {
		
		List<String> groups = new ArrayList<String>();
		String statement = "SELECT * FROM " + MySQLManager.playersGroupsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				groups.add(rs.getString("parent"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		List<String> permissions = new ArrayList<String>();
		statement = "SELECT * FROM " + MySQLManager.playersPermissionsTable + " WHERE uuid=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, u.toString());

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				permissions.add(rs.getString("permission"));
			}
		} catch (SQLException e) { e.printStackTrace(); }
		
		for(String group : groups) {
			List<String> permsToAdd = getPermissionsOfParents(new ArrayList<String>(), group);
			permissions.addAll(permsToAdd);
		}
		return permissions;
	}
	
	public List<String> getPermissionsOfParents(List<String> permissions, String group) {
		for(String s : Main.getMySQLManager().getStorageGroupGetterSetter().getGroupPermissions(group)) {
			permissions.add(s);
		}
		
		if(Main.getMySQLManager().getStorageGroupGetterSetter().getGroupParents(group).size() == 0) return permissions;
		
		for(String parentGroup : Main.getMySQLManager().getStorageGroupGetterSetter().getGroupParents(group)) {
			List<String> permissionsToAdd = getPermissionsOfParents(new ArrayList<String>(), parentGroup);
			permissions.addAll(permissionsToAdd);
		}
		
		return permissions;
	}
}
