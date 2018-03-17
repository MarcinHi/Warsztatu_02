package pl.workshop_02_controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pl.workshop_02.db.DbUtil;
import pl.workshop_02.db.GroupDao;

public class MainProgram3 {

	// Admin controller of Users Groups
	
	public static void main(String[] args) {
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Witaj w programie 3.");

		String response = "";
		while (!"quit".equals(response)) {
			printAllGroups();
			response = mainQuestion(read);
			switch (response) {
			case "add":
				addGroup(read);
				break;
			case "edit":
				editGroup(read);
				break;
			case "delete":
				deleteGroup(read);
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

	private static void printAllGroups() {
		System.out.println("Lista grup:");
		System.out.println("ID / Name");
		List<GroupDao> groupList = new ArrayList<>();
		try (Connection conn = DbUtil.getConn()) {
			groupList = GroupDao.loadAllgroups(conn);
		} catch (SQLException e) {
			System.out.println("Database connection ERROR!");
		}

		for (GroupDao gd : groupList) {
			System.out.println(gd.getId() + " / " + gd.getName());
		}
		System.out.println("----------------------------------------------------------------------------");
	}

	private static String mainQuestion(BufferedReader read) {
		String response = "";

		System.out.println("Wybierz jedna z opcji:");
		System.out.println("add    - dodanie grupy");
		System.out.println("edit   - edycja grupy");
		System.out.println("delete - usuniecie grupy");
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

	private static void addGroup(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			System.out.println("Podaj nazwe nowej grupy:");
			String name = read.readLine();
			GroupDao newGroup = new GroupDao(0, name);
			newGroup.saveToDB(conn);
			System.out.println("Dodano nowa grupe do bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("add Group ERROR");
			e.printStackTrace();
		}

	}

	private static void editGroup(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			System.out.println("Ktora grupe chcesz edytowac? Podaj jej id:");
			int id = Integer.parseInt(read.readLine());
			GroupDao gd = GroupDao.loadGroupById(conn, id);
			System.out.println("edytujemy grupe:");
			System.out.println(gd.getId()+ " / " + gd.getName());
			System.out.println("Podaj nowa nazwe dla tej grupy:");
			String name = read.readLine();
			GroupDao modifiedGroup = new GroupDao(id, name);
			modifiedGroup.saveToDB(conn);
			System.out.println("Dodano zmodyfikowana grupe do bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("edit Group ERROR");
			e.printStackTrace();
		}

	}

	private static void deleteGroup(BufferedReader read) {
		try (Connection conn = DbUtil.getConn()) {
			System.out.println("Ktora grupe chcesz usunac? Podaj jej id:");
			int id = Integer.parseInt(read.readLine());
			GroupDao gd = GroupDao.loadGroupById(conn, id);
			gd.delete(conn);
			System.out.println("Usunieto grupe z bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();

		} catch (IOException | SQLException e) {
			System.out.println("delete Group ERROR");
			e.printStackTrace();
		}


	}

}
