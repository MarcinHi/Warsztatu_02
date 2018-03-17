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
import pl.workshop_02.db.UserDao;

public class MainProgram1 {

	//Admin controller for the user
	
	public static void main(String[] args) {
		BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Witaj w programie 1.");
		
		String response = "";
		while(!"quit".equals(response)) {
			printAllUsers();
			response = mainQuestion(read);
			switch (response) {
			case "add":
				addUser(read);
				break;
			case "edit":
				editUser(read);
				break;
			case "delete":
				deleteUser(read);
				break;
			case "quit":
				break;
			default:
				System.out.println("Bledna komenda! Try again!");
			}
		} 
		System.out.println("Bye!");
		try{
			read.close();
		} catch(IOException e) {
			e.printStackTrace();
			System.out.println("reader closing problem!");
		}
	}
	
	private static void printAllUsers() {
		System.out.println("Lista uzytkownikow:");
		System.out.println("ID / User name / email / password / group");
		List<UserDao> usersList = new ArrayList<>();
		try(Connection conn = DbUtil.getConn()){
			usersList = UserDao.loadAllUsers(conn);
		} catch(SQLException e) {
			System.out.println("Database connection ERROR!");
		}
		
		for(UserDao ud : usersList) {
			System.out.println(ud.getId() + " / " + ud.getUsername() + " / " + ud.getEmail() + " / " + ud.getPassword() + " / " + ud.getGroup());
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
	
	private static void addUser(BufferedReader read) {
		try(Connection conn = DbUtil.getConn()) {
			System.out.println("Podaj imie i nazwisko nowego uzytkownika:");
			String name = read.readLine();
			System.out.println("Podaj haslo:");
			String password = read.readLine();
			System.out.println("Podaj e-mail:");
			String mail = read.readLine();
			System.out.println("Do ktorej grupy przypisac uzytkownika?");
			List<GroupDao> list = GroupDao.loadAllgroups(conn);
			for(GroupDao gd : list) {
				System.out.println("ID: " + gd.getId() + " / Group name: " + gd.getName());
			}
			System.out.println("Podaj ID: ");
			int group = Integer.parseInt(read.readLine());
			UserDao newUser = new UserDao(0, name, password, mail, group);
			newUser.saveToDB(conn);
			System.out.println("Dodano uzytkownika do bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();
		
		} catch (IOException | SQLException e) {
			System.out.println("add User ERROR");
			e.printStackTrace();
		}
		
	}
	
	private static void editUser(BufferedReader read) {
		try(Connection conn = DbUtil.getConn()) {
			System.out.println("Ktorego uzytkownika chcesz edytowac?");
			int id = Integer.parseInt(read.readLine());
			UserDao ud = UserDao.loadUserById(conn, id);
			System.out.println("edytujemy uzytkownika:");
			System.out.println(ud.getId() + " / " + ud.getUsername() + " / " + ud.getEmail() + " / " + ud.getPassword() + " / " + ud.getGroup());
			System.out.println("Podaj nowe imie i nazwisko:");
			String name = read.readLine();
			System.out.println("Podaj nowe haslo:");
			String password = read.readLine();
			System.out.println("Podaj nowy e-mail:");
			String mail = read.readLine();
			System.out.println("Do ktorej grupy przypisac uzytkownika?");
			List<GroupDao> list = GroupDao.loadAllgroups(conn);
			for(GroupDao gd : list) {
				System.out.println("ID: " + gd.getId() + " / Group name: " + gd.getName());
			}
			System.out.println("Podaj ID: ");
			int group = Integer.parseInt(read.readLine());
			UserDao modifiedUser = new UserDao(id, name, password, mail, group);
			modifiedUser.saveToDB(conn);
			System.out.println("Dodano uzytkownika do bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();
		
		} catch (IOException | SQLException e) {
			System.out.println("edit User ERROR");
			e.printStackTrace();
		}
		
	}
	
	private static void deleteUser(BufferedReader read) {
		try(Connection conn = DbUtil.getConn()) {
			System.out.println("Ktorego uzytkownika chcesz usunac?");
			int id = Integer.parseInt(read.readLine());
			UserDao ud = UserDao.loadUserById(conn, id);
			ud.delete(conn);
			System.out.println("Usunieto uzytkownika z bazy danych. Kliknij ENTER aby kontynuowac.");
			read.readLine();
		
		} catch (IOException | SQLException e) {
			System.out.println("delete User ERROR");
			e.printStackTrace();
		}
	}

}
