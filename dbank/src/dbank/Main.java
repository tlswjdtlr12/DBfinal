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
		// project에 builds 지정 필수
		//String url ="jdbc:mysql://127.0.0.1:3306/company";
		String url ="jdbc:mariadb://127.0.0.1:3306/company";
		String user="tlswjdtlr1";
		String password ="showme12";  

		//Driver load.    
		try{
			Class.forName("org.mariadb.jdbc.Driver");          
		}catch(ClassNotFoundException e){
			System.out.println("드라이브 설정을 찾을 수 없습니다.");
		}

		//Connection.
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url,user,password);
			//--==>> Connection 개체를 반환한다.

			stmt = conn.createStatement();
			sc = new Scanner(System.in);
			Users users = new Users();
			users.run();
			//SQL
		} catch (SQLException e) {
			System.out.println("DB접속 중 오류가 발생하였습니다.");
			e.printStackTrace();
		}
	}
}