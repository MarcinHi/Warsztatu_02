package pl.workshop_02.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbUtil {

	public static Connection getConn(){
		try{
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/school?useSSL=false","root","coderslab");
			return conn;
		} catch(SQLException e) {
			System.out.println("Database connection ERROR!");
			return null;
		}
	}

	
}
