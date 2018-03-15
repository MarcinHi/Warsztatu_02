package pl.workshop_02.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {

	public static void main(String[] args) {
//		UserDao firstUser = new UserDao("Trzeci", "dupadupadupa", "dupa3@dupa.com");
			try(Connection conn = DbUtil.getConn()) {

				UserDao modifyUser = new UserDao();
				modifyUser = UserDao.loadUserById(conn, 5);
				modifyUser.delete(conn);
				
				System.out.println("done");
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("Database update ERROR!");
			}
	}

}
