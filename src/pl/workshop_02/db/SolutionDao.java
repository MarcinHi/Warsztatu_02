package pl.workshop_02.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SolutionDao {
	private int id;
	private String created;
	private String updated;
	private String description;
	private int exerciseId;
	private int usersId;
	
	public SolutionDao() {
	}

	public SolutionDao(String created, String updated, String description, int exerciseId, int usersId) {
		this.created = created;
		this.updated = updated;
		this.description = description;
		this.exerciseId = exerciseId;
		this.usersId = usersId;
	}
	
	public String getCreated() {
		return created;
	}
	
	public void setCreated(String created) {
		this.created = created;
	}
	
	public String getUpdated() {
		return updated;
	}
	
	public void setUpdated(String updated) {
		this.updated = updated;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getExerciseId() {
		return exerciseId;
	}
	
	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}
	public int getUsersId() {
		return usersId;
	}
	public void setUsersId(int usersId) {
		this.usersId = usersId;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	static public List<SolutionDao> loadAllSolutions(Connection conn) throws SQLException {
		String sql = "SELECT * FROM solution";
		List<SolutionDao> solutions = new ArrayList<>();
		PreparedStatement preStm = conn.prepareStatement(sql);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			SolutionDao loadedSolution = new SolutionDao();
			loadedSolution.id = rs.getInt("id");
			loadedSolution.created = rs.getString("created");
			loadedSolution.updated = rs.getString("updated");
			loadedSolution.description = rs.getString("description");
			loadedSolution.exerciseId = rs.getInt("exercise_id");
			loadedSolution.usersId = rs.getInt("users_id");
			solutions.add(loadedSolution);
		}
		return solutions;
	}

	static public SolutionDao loadSolutionById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM solution WHERE id = ?";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			SolutionDao loadedSolution = new SolutionDao();
			loadedSolution.id = rs.getInt("id");
			java.util.Date dateCreated = new java.util.Date();
			java.util.Date dateUpdated = new java.util.Date();
			dateCreated = rs.getDate("created");
			dateUpdated = rs.getDate("updated");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String created = sdf.format(dateCreated);
			String updated = sdf.format(dateUpdated);
			loadedSolution.created = created;
			loadedSolution.updated = updated;
			loadedSolution.description = rs.getString("description");
			loadedSolution.exerciseId = rs.getInt("exercise_id");
			loadedSolution.usersId = rs.getInt("users_id");
			return loadedSolution;
		}
		return null;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO solution(created, updated, description, exercise_id, users_id) VALUE (?, ?, ?, ?, ?)";
			String generatedColumns[] = { "id" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.created);
			preparedStatement.setString(2, this.updated);
			preparedStatement.setString(3, this.description);
			preparedStatement.setInt(4, this.exerciseId);
			preparedStatement.setInt(5, this.usersId);
			
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE	solution	SET	created = ?, updated = ?, description =?, exercise_id = ?, users_id = ?	where	id	=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.created);
			preparedStatement.setString(2, this.updated);
			preparedStatement.setString(3, this.description);
			preparedStatement.setInt(4, this.exerciseId);
			preparedStatement.setInt(5, this.usersId);
			preparedStatement.setInt(6, this.id);
			preparedStatement.executeUpdate();
		}
	}


	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE	FROM	solution WHERE	id=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}
	
	public List<SolutionDao> loadAllByExerciseId(int id, Connection conn) throws SQLException{
		List<SolutionDao> list = new ArrayList<>();
		String sql = "SELECT * FROM solution WHERE exercise_id = ? ORDER BY updated DESC";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		while(rs.next()) {
			SolutionDao loadedSolution = new SolutionDao();
			loadedSolution.setId(rs.getInt("id"));
			loadedSolution.setCreated(rs.getString("created"));
			loadedSolution.setUpdated(rs.getString("updated"));
			loadedSolution.setDescription("description");
			loadedSolution.setExerciseId(rs.getInt("exercise_id"));
			loadedSolution.setUsersId(rs.getInt("users_id"));
			list.add(loadedSolution);
		}
		return list;
	}

}
