package dbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	public static Scanner sc;
	public static Statement stmt;
	public static void main(String[] args) throws Exception{
		String url ="jdbc:mariadb://127.0.0.1:3306/company";
		String user="k4i957HLkqEpUaSbHL6uRA==";
		String password ="PqGX5CVHXA2pCT0YMoHSng==";  

		//Driver load.    
		try{
			Class.forName("org.mariadb.jdbc.Driver");          
		}catch(ClassNotFoundException e){
			System.out.println("드라이브 설정을 찾을 수 없습니다.");
		}

		//Connection.
		try {
			Users users = new Users();
			Connection conn = DriverManager.getConnection(url,Users.decAES(user),Users.decAES(password));
			stmt = conn.createStatement();
			sc = new Scanner(System.in);
			
			users.run();
		} catch (SQLException e) {
			System.out.println("DB접속 중 오류가 발생하였습니다.");
			e.printStackTrace();
		}
	}
}