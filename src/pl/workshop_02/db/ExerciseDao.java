package pl.workshop_02.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseDao {

	private int id;
	private String title;
	private String description;

	public ExerciseDao() {
	}

	public ExerciseDao(int id, String title, String description) {
		this.id = id;
		this.title = title;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	static public List<ExerciseDao> loadAllExercises(Connection conn) throws SQLException {
		String sql = "SELECT * FROM exercise";
		List<ExerciseDao> exercises = new ArrayList<>();
		PreparedStatement preStm = conn.prepareStatement(sql);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			ExerciseDao loadedExercise = new ExerciseDao();
			loadedExercise.id = rs.getInt("id");
			loadedExercise.title = rs.getString("title");
			loadedExercise.description = rs.getString("description");
			exercises.add(loadedExercise);
		}
		return exercises;
	}

	static public ExerciseDao loadExerciseById(Connection conn, int id) throws SQLException {
		String sql = "SELECT * FROM exercise WHERE id = ?";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			ExerciseDao loadedExercise = new ExerciseDao();
			loadedExercise.id = rs.getInt("id");
			loadedExercise.title = rs.getString("title");
			loadedExercise.description = rs.getString("description");
			return loadedExercise;
		}
		return null;
	}

	public void saveToDB(Connection conn) throws SQLException {
		if (this.id == 0) {
			String sql = "INSERT INTO exercise(title, description) VALUE (?, ?)";
			String generatedColumns[] = { "id" };
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql, generatedColumns);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			if (rs.next()) {
				this.id = rs.getInt(1);
			}
		} else {
			String sql = "UPDATE	exercise	SET	title = ?, description = ?	where	id	=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, this.title);
			preparedStatement.setString(2, this.description);
			preparedStatement.setInt(3, this.id);
			preparedStatement.executeUpdate();
		}
	}

	public void delete(Connection conn) throws SQLException {
		if (this.id != 0) {
			String sql = "DELETE	FROM	exercise WHERE	id=	?";
			PreparedStatement preparedStatement;
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, this.id);
			preparedStatement.executeUpdate();
			this.id = 0;
		}
	}

	public List<SolutionDao> loadAllSolutionsByUserId(int id, Connection conn) throws SQLException {
		List<SolutionDao> list = new ArrayList<>();
		String sql = "SELECT * FROM solution WHERE users_id = ? AND exercise_id = ?";
		PreparedStatement preStm = conn.prepareStatement(sql);
		preStm.setInt(1, id);
		preStm.setInt(2, this.id);
		ResultSet rs = preStm.executeQuery();
		while (rs.next()) {
			SolutionDao loadedSolution = new SolutionDao();
			loadedSolution.setSolutionId(rs.getInt("id"));
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
