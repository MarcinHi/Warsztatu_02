package pl.workshop_02.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

public class UserDao {
	private int id;
	private String username;
	private String password;
	private String email;
	private int group;

	public UserDao() {
	}

	public UserDao(int id, String username, String password, String email, int group) {
		this.id = id;
		this.username = username;
		this.setPassword(password);
		this.email = email;
		this.group = group;
	}


	static public List<UserDao> loadAllUsers(Connection conn) throws SQLException {
		String sql = "SELECT * FROM users";
		List<UserDao> users = new ArrayList<>();
		PreparedStatement preStm = conn.prepareStatement(sql);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			UserDao loadedUser = new UserDao();
			loadedUser.id = rs.getInt("id");
			loadedUser.username = rs.getString("username");
			loadedUser.email = rs.getString("email");
			loadedUser.password = rs.getString("password");
			loadedUser.group = rs.getInt("person_group_id");
			users.add(loadedUser);
		}
		return users;
	}

	static public UserDao loadUserById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM users WHERE id = ?";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			UserDao loadedUser = new UserDao();
			loadedUser.id = rs.getInt("id");
			loadedUser.username = rs.getString("username");
			loadedUser.email = rs.getString("email");
			loadedUser.password = rs.getString("password");
			loadedUser.group = rs.getInt("person_group_id");
			return loadedUser;
		}
		return null;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO users(username, email, password, person_group_id) VALUES (?, ?, ?, ?)";
			String generatedColumns[] = { "id" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.group);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE	users	SET	username=?,	email=?, password=?, person_group_id=?	where	id=?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.username);
			preparedStatement.setString(2, this.email);
			preparedStatement.setString(3, this.password);
			preparedStatement.setInt(4, this.group);
			preparedStatement.setInt(5, this.id);
			preparedStatement.executeUpdate();
		}
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE	FROM	users	WHERE	id=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public List<UserDao> loadAllByGrupId(int id, Connection conn) throws SQLException {
		List<UserDao> list = new ArrayList<>();
		String sql = "SELECT * FROM users WHERE person_group_id = ?";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			UserDao loadedUser = new UserDao();
			loadedUser.id = rs.getInt("id");
			loadedUser.username = rs.getString("username");
			loadedUser.email = rs.getString("email");
			loadedUser.password = rs.getString("password");
			list.add(loadedUser);
		}
		return list;
	}

}
