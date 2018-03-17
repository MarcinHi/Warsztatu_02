package pl.workshop_02_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.workshop_02.db.DbUtil;
import pl.workshop_02.db.ExerciseDao;

public class MainProgram2 {

	// Admin controller of Exercises

	public static void main(String[] args) {
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Witaj w programie 2.");

		String response = "";
		while (!"quit".equals(response)) {
			printAllExercises();
			response = mainQuestion(read);
			switch (response) {
			case "add":
				addExercise(read);
				break;
			case "edit":
				editExercise(read);
				break;
			case "delete":
				deleteExercise(read);
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

	private static String mainQuestion(BufferedReader read) {
		String response = "";

		System.out.println("Wybierz jedna z opcji:");
		System.out.println("add    - dodanie uzytkownika");
		System.out.println("edit   - edycja uzytkownika");
		System.out.println("delete - usuniecie uzytkownika");
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

	private static void addExercise(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			System.out.println("Podaj nazwe nowego zadania:");
			String title = read.readLine();
			System.out.println("Podaj opis zadania:");
			String description = read.readLine();
			ExerciseDao newExercise = new ExerciseDao(0, title, description);
			newExercise.saveToDB(conn);
			System.out.println("Dodano nowe zadanie do bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("add User ERROR");
			e.printStackTrace();
		}

	}

	private static void editExercise(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			System.out.println("Ktore zadanie chcesz edytowac?");
			int id = Integer.parseInt(read.readLine());
			ExerciseDao ed = ExerciseDao.loadExerciseById(conn, id);
			System.out.println("edytujemy zadanie:");
			System.out.println(ed.getTitle() + " / " + ed.getDescription());
			System.out.println("Podaj nowa nazwe dla tego zadania:");
			String name = read.readLine();
			System.out.println("Podaj nowy opis tego zadania:");
			String description = read.readLine();
			ExerciseDao modifiedExercise = new ExerciseDao(id, name, description);
			modifiedExercise.saveToDB(conn);
			System.out.println("Dodano zmodyfikowane  zadanie do bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("edit User ERROR");
			e.printStackTrace();
		}

	}

	private static void deleteExercise(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			System.out.println("Ktore zadanie chcesz usunac? Podaj jego id:");
			int id = Integer.parseInt(read.readLine());
			ExerciseDao ed = ExerciseDao.loadExerciseById(conn, id);
			ed.delete(conn);
			System.out.println("Usunieto zadanie z bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("delete User ERROR");
			e.printStackTrace();
		}

	}

}
