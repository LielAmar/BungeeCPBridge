package com.lielamar.completepermissions.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLGroupGetterSetter {

	private Connection connection;
	public MySQLGroupGetterSetter(Connection connection) { this.connection = connection; }
	public Connection getConnection() { return this.connection; }
	
	public List<String> getGroupPermissions(String group) {
		List<String> permissions = new ArrayList<String>();
		
		String statement = "SELECT * FROM " + MySQLManager.groupsPermissionsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				permissions.add(rs.getString("permission"));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return permissions;
	}
	
	public List<String> getGroupParents(String group) {
		List<String> parentsRaw = new ArrayList<String>();
		
		String statement = "SELECT * FROM " + MySQLManager.groupsParentsTable + " WHERE name=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(statement);
			ps.setString(1, group);
			ResultSet rs = ps.executeQuery();
			while (rs.next())
				parentsRaw.add(rs.getString("parent"));
		} catch (SQLException e) { e.printStackTrace(); }
		
		return parentsRaw;
	}
}
