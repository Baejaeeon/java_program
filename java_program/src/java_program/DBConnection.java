package java_program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

	private Connection conn;
	private Statement stmt;
	
	//private DBConnection() {
		// TODO Auto-generated constructor stub
	//};
	
	private static class SingleHolder {
		public static DBConnection single = new DBConnection();
	}
	
	public static DBConnection getInstance() {
		return SingleHolder.single;
	}
	
	public boolean ConnectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String uri = "jdbc:mysql://52.79.59.96:14000/groupware";
			String id = "groupware";
			String pwd = "groupware!@#";
			
			conn = DriverManager.getConnection(uri, id, pwd);
			stmt = conn.createStatement();
			
			System.out.println("Database Connected...!");
			
			stmt.close();
			conn.close();
			return true;
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return false;
	}
}
