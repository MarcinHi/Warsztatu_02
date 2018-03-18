package pl.workshop_02_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import pl.workshop_02.db.DbUtil;
import pl.workshop_02.db.ExerciseDao;
import pl.workshop_02.db.SolutionDao;
import pl.workshop_02.db.UserDao;

public class UserController {

	public static void main(String[] args) {

		System.out.println("Witaj Uzytkowniku.");
		String response = "";
		try (BufferedReader read = new BufferedReader(new InputStreamReader(System.in))) {
			printAllUsers();
			System.out.println("Podaj swoj login");
			int userId = Integer.parseInt(read.readLine());
			while (!"quit".equals(response)) {
				System.out.println("Wybierz jedna z opcji:");
				System.out.println("add  - Dodawanie rozwiazan");
				System.out.println("view - Przegladanie swoich rozwiazan");
				System.out.println("quit - Wyjscie z programu");
				System.out.println();
				System.out.println("Wpisz komende i nacisnij ENTER:");
				response = read.readLine();
				switch (response) {
				case "add":
					addSolutionToExercise(userId, read);
					break;
				case "view":
					printAllExercisesByUserId(read, userId);
					System.out.println("Rozwiazania ktorego zadania chcesz zobaczyc? Podaj ID zadania:");
					int exerciseId = Integer.parseInt(read.readLine());
					ExerciseDao exercise = new ExerciseDao();
					try (Connection conn = DbUtil.getConn()) {
						exercise = ExerciseDao.loadExerciseById(conn, exerciseId);
						List<SolutionDao> solutionsList = exercise.loadAllSolutionsByUserId(userId, conn);
						for (SolutionDao sol : solutionsList) {
							System.out.println(sol.getId() + " / " + sol.getCreated() + " / " + sol.getUpdated() + " / "
									+ sol.getDescription());
						}
					} catch (SQLException e) {
						System.out.println("Database connection problem!");
					}
					break;
				case "quit":
					break;
				default:
					System.out.println("Bledna komenda! Try again!");
				}
			}
			System.out.println("Bye!");
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("reader closing problem!");
		}
	}

	private static void printAllUsers() {
		System.out.println("Lista uzytkownikow:");
		System.out.println("ID / User name / email / password / group");
		List<UserDao> usersList = new ArrayList<>();
		try (Connection conn = DbUtil.getConn()) {
			usersList = UserDao.loadAllUsers(conn);
		} catch (SQLException e) {
			System.out.println("Database connection ERROR!");
		}

		for (UserDao ud : usersList) {
			System.out.println(ud.getId() + " / " + ud.getUsername() + " / " + ud.getEmail() + " / " + ud.getPassword()
					+ " / " + ud.getGroup());
		}
		System.out.println("----------------------------------------------------------------------------");
	}

	private static void printAllExercisesByUserId(BufferedReader read, int id) // where there is no solution yet
	{
		String sql = "select * from exercise INNER JOIN solution ON exercise.id = solution.exercise_id WHERE solution.users_id = ?";
		try (Connection conn = DbUtil.getConn()) {
			PreparedStatement preStm = conn.prepareStatement(sql);
			preStm.setInt(1, id);
			ResultSet rs = preStm.executeQuery();
			while (rs.next()) {
				System.out
						.println(rs.getInt("id") + " / " + rs.getString("title") + " / " + rs.getString("description"));
			}
		} catch (SQLException e) {
			System.out.println("Database connection error at printAllExercisesByUserId");
			e.printStackTrace();
		}
	}

	private static void printAllOpenExercisesByUserId(BufferedReader read, int id) // where there is no solution yet
	{
		String sql = "select * from exercise INNER JOIN solution ON exercise.id = solution.exercise_id WHERE solution.users_id = ? AND solution.updated IS NULL";
		try (Connection conn = DbUtil.getConn()) {
			PreparedStatement preStm = conn.prepareStatement(sql);
			preStm.setInt(1, id);
			ResultSet rs = preStm.executeQuery();
			while (rs.next()) {
				System.out
						.println(rs.getInt("id") + " / " + rs.getString("title") + " / " + rs.getString("description"));
			}
		} catch (SQLException e) {
			System.out.println("Database connection error at printAllExercisesByUserId");
			e.printStackTrace();
		}
	}

	private static void addSolutionToExercise(int userId, BufferedReader read) {
		System.out.println("Nie dodales rozwiazan do nastepujacych zadan:");
		printAllOpenExercisesByUserId(read, userId);
		System.out.println("Do ktorego chcesz dodac rozwiazanie?");
		try (Connection conn = DbUtil.getConn()) {
			int exerciseId = Integer.parseInt(read.readLine());
			System.out.println("Podaj swoje rozwiazanie i nacisnij ENTER");
			String description = read.readLine();

			SolutionDao solution = new SolutionDao();
			java.util.Date dateUpdated = new java.util.Date();
			java.util.Date dateCreated = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updated = sdf.format(dateUpdated);
			String created = sdf.format(dateCreated);

			String sql = "select * from solution WHERE exercise_id = ? AND users_id = ?";

			PreparedStatement preStm = conn.prepareStatement(sql);
			preStm.setInt(1, exerciseId);
			preStm.setInt(2, userId);
			ResultSet rs = preStm.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				dateCreated = rs.getDate("created");
				String created = sdf.format(createdDate);
				solution.setId(id);
				solution.setCreated(created);
				solution.setUpdated(updated);
				solution.setDescription(description);
				solution.setExerciseId(exerciseId);
				solution.setUsersId(userId);
				solution.saveToDB(conn);
			}

		} catch (NumberFormatException | IOException | SQLException e) {
			System.out.println("addSolutionToExercise ERROR!");
			e.printStackTrace();
		}

	}

}
