package pl.workshop_02.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {
	private int id;
	private String name;
	
	public GroupDao(String name) {
		this.name = name;
	}
	public GroupDao() {
	}

	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	static public List<GroupDao> loadAllgroups (Connection conn) throws SQLException {
		String sql = "SELECT * FROM user_group";
		List<GroupDao> groups = new ArrayList<>();
		PreparedStatement preStm = conn.prepareStatement(sql);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			GroupDao loadedGroup = new GroupDao();
			loadedGroup.id = rs.getInt("id");
			loadedGroup.name = rs.getString("name");
			groups.add(loadedGroup);
		}
		return groups;
	}

	static public GroupDao loadGroupById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM user_group WHERE id = ?";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			GroupDao loadedGroup = new GroupDao();
			loadedGroup.id = rs.getInt("id");
			loadedGroup.name = rs.getString("name");
			return loadedGroup;
		}
		return null;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO user_group(name) VALUE (?)";
			String generatedColumns[] = { "id" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.name);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE	user_group	SET	name=?,	where	id	=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.name);
			preparedStatement.setInt(2, this.id);
			preparedStatement.executeUpdate();
		}
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE	FROM	user_group WHERE	id=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}
	
	
}
