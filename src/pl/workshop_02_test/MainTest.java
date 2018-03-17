package pl.workshop_02_test;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.workshop_02.db.DbUtil;
import pl.workshop_02.db.ExerciseDao;
import pl.workshop_02.db.GroupDao;
import pl.workshop_02.db.SolutionDao;
import pl.workshop_02.db.UserDao;

public class MainTest {

	public static void main(String[] args) {

//		UserDao user = new UserDao("testowy3", "jakieshaslo", "jakis3@email.com");
//		ExerciseDao exercise = new ExerciseDao("Cwiczenie testowe3", "jakis opis");
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String now = sdf.format(date);
//		SolutionDao solution = new SolutionDao(now, now, "jakis opis jakiegos rozwiazania", 2, 2);
//		GroupDao group = new GroupDao("grupa testowa3");
		
		try(Connection conn = DbUtil.getConn()){
//			SolutionDao solution = new SolutionDao();
//			List<SolutionDao> list = new ArrayList<>();
//			list = solution.loadAllByExerciseId(2, conn);
//			for(SolutionDao sol : list) {
//				System.out.println("solution: " + sol.getId() + " " + sol.getCreated() + " " + sol.getUpdated() + " " + sol.getDescription() +" " + sol.getExerciseId() + " " + sol.getUsersId());
//
//			}
			
			UserDao user = new UserDao();
			List<UserDao> list = new ArrayList<>();
			GroupDao group = new GroupDao();
			group = group.loadGroupById(conn, 2);
			System.out.println("Dla grupy numer: 2 o nazwie: " + group.getName());
			
			list = user.loadAllByGrupId(2, conn);
			for(UserDao ud : list) {
				System.out.println("user: " + ud.getId() + " " + ud.getUsername() + " " + ud.getEmail() + " " + ud.getPassword());
			}
//		user.saveToDB(conn);
//		exercise.saveToDB(conn);
//		solution.saveToDB(conn);
//		group.saveToDB(conn);
		
//		UserDao user1 = UserDao.loadUserById(conn, 1);
//		ExerciseDao exercise1 = ExerciseDao.loadExerciseById(conn, 1);
//		SolutionDao solution1 = SolutionDao.loadSolutionById(conn, 3);
//		GroupDao group1 = GroupDao.loadGroupById(conn, 3);
		
//		System.out.println("user: " + user1.getUsername() + " " + user1.getId() + " " + user1.getEmail());
//		System.out.println("exercise: " + exercise1.getId() + " " + exercise1.getTitle() + " " + exercise1.getDescription());
//		System.out.println("solution: " + solution1.getId() + " " + solution1.getCreated() + " " + solution1.getUpdated() + " " + solution1.getDescription() +" " + solution1.getExerciseId() + " " +solution1.getUsersId());
//		System.out.println("group: " + group1.getId() + " " + group1.getName());
		} catch(SQLException e) {
			System.out.println("Database input ERROR!");
			e.printStackTrace();
		}
	}

}
