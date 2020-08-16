package utils;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class ConnectionsUtils {
	static Connection connection = null;
	static String databasename = "atmdb";
	static String url = "jdbc:mysql://localhost:3306/" + databasename + "?autoReconnect=true&useSSL=false";
	
	static String username = "root";
	static String password = "admin";
	
	@SuppressWarnings("deprecation")
	public static Connection connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url, username, password);
			return connection;
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
			return null;
		}
	}
	public static void main(String[] args){
		// TODO Auto-generated method stub
		
	}

}
