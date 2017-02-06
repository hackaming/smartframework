package Test;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBUtil {
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://10.62.150.57:3306/demo";
	private static final String username = "root";
	private static final String password = "hackaming";
	
	private static Connection conn = null;
	
	public static Connection getConnection(){
		try{
			Class.forName(driver);
			conn = DriverManager.getConnection(url,username,password);
		} catch (Exception e){
			e.printStackTrace();
		}
		return conn;
	}
	public static void closeConnection(){
		try {
			if (null == conn){
				conn.close();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
