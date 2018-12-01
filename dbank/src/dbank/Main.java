package dbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	public static Scanner sc;
    public static void main(String[] args){
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
        } catch (SQLException e) {
            System.out.println("DB접속 중 오류가 발생하였습니다.");
            e.printStackTrace();
        }
        
        sc = new Scanner(System.in);
        //SQL
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * ");
        sqlBuilder.append(" FROM document ");
        
        //--==>> Database에서 수행 가능한 객체로 변환시켜주는 구문.
        Statement stmt =null;
        ResultSet rs = null; //--==>>sun에서 제공하는 API 중 하나
                             //--==>>커서가 이동한 후 데이터의 유/무를 판단해서 true/false 반환
        try {
            stmt = conn.createStatement();
            
            //SQL execute
            rs = stmt.executeQuery(sqlBuilder.toString());
            //--==>>위에서 정의한 String을 실행
            while(rs.next()){
                //String empNo = rs.getString("empNo"); // =  rs.getString(1); : 첫 번째 데이터 주세요
                                                      //--==>> 컬럼명을 직접 명시해 주는 것이 더 낫다. (확인 불필요 등등...)
               
                //int salary = rs.getInt("salary");
            	String Fname = rs.getString("registerday");
            	String Minit = rs.getString("Dname");
            	String Lname = rs.getString("Daddr");
            	System.out.println(Fname + " " + Minit + " " + Lname);
            }
        } catch (SQLException e) {
            System.out.println("SQL 수행중 오류가 발생했습니다.");
        }
              
        // xor암호 : https://blog.naver.com/zkd1750/90192019205
        /* 
          계좌번호 생성

nAccount="110-499-"+(rand.nextInt(500000)+100000);
					try {
						String qry=String.format("insert into account values "
								+ "('%s','%s',0,'%s','%s')", nAccount, password, sDate, bank);
						//System.out.println(qry);
						Main.stmt.executeUpdate(qry);
						break;
					}
					catch(Exception e) {}


*/
    }
}