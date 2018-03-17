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

public class MainProgram4 {

	// Admin controller of Exercises/Solutions for Users

	public static void main(String[] args) {

		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Witaj w programie 4.");

		String response = "";
		while (!"quit".equals(response)) {
			response = mainQuestion(read);
			switch (response) {
			case "add":
				addExerciseForUser(read);
				break;
			case "view":
				viewUsersSolutions(read);
				break;
			case "quit":
				break;
			default:
				System.out.println("Bledna komenda! Try again!");
			}
		}
		System.out.println("Bye!");
		try {
			read.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("reader closing problem!");
		}
	}

	private static void printAllExercises() {
		System.out.println("Lista zadan:");
		System.out.println("ID / Title / Description");
		List<ExerciseDao> exercisesList = new ArrayList<>();
		try (Connection conn = DbUtil.getConn()) {
			exercisesList = ExerciseDao.loadAllExercises(conn);
		} catch (SQLException e) {
			System.out.println("Database connection ERROR!");
		}

		for (ExerciseDao ed : exercisesList) {
			System.out.println(ed.getId() + " / " + ed.getTitle() + " / " + ed.getDescription());
		}
		System.out.println("----------------------------------------------------------------------------");
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

	private static String mainQuestion(BufferedReader read) {
		String response = "";

		System.out.println("Wybierz jedna z opcji:");
		System.out.println("add    - przypisanie zadan do uzytkownikow");
		System.out.println("view   - przegladanie rozwiazan danego uczestnika");
		System.out.println("quit   - zakonczenie programu");
		System.out.println("Wpisz komende i nacisnij enter:");

		try {
			response = read.readLine();
		} catch (IOException e) {
			System.out.println("reader ERROR");
			e.printStackTrace();
		}
		return response;
	}

	private static void addExerciseForUser(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			printAllUsers();
			System.out.println("Ktoremu uzytkownikowi chcesz przypisac zadanie? Podaj jego id:");
			int userId = Integer.parseInt(read.readLine());
			printAllExercises();
			System.out.println("Ktore zadanie przypisac?");
			int exerciseId = Integer.parseInt(read.readLine());
			java.util.Date dateCreated = new java.util.Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String created = sdf.format(dateCreated);
			SolutionDao newSolution = new SolutionDao(0, created, null, null, exerciseId, userId);
			newSolution.saveToDB(conn);
			System.out.println("Przypisane. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("add Group ERROR");
			e.printStackTrace();
		}

	}

	private static void viewUsersSolutions(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			printAllUsers();
			System.out.println("Rozwiazania ktorego uzytkownika chcesz zobaczyc?");
			int id = Integer.parseInt(read.readLine());
			String sql = "SELECT * FROM solution WHERE users_id = ?";
			PreparedStatement preStm = conn.prepareStatement(sql);
			preStm.setInt(1, id);
			ResultSet rs = preStm.executeQuery();
			int count = 0;
			System.out.println("ID  /  Creation date / Update date / description / exercise_id / users_id");
			while (rs.next()) {
				System.out.println(rs.getInt("id") + " / " + rs.getString("created") + " / " + rs.getString("updated")
						+ " / " + rs.getString("description") + " / " + rs.getInt("exercise_id") + " / "
						+ rs.getInt("users_id"));
				count++;
			}
			if (count == 0) {
				System.out.println("Brak przypisanych zadan do tego uzytkownika!");
				return;
			}
		} catch (NumberFormatException | IOException | SQLException e) {
			e.printStackTrace();
			System.out.println("viewUsersSolutions ERROR");
		}
	}

}
