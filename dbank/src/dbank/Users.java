package dbank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;

public class Users {

	public void run() {
		String cmd;
		String password;
		String start_day;
		String acuid; // 계정 번호
		String Anumber; // 계좌 번호
		String name;
		String address;
		String phone;
		String birth; // yyyy-mm-dd 형식
		String bnumber; // branch number (예시 주어주고 고르라고 해야함)
		Boolean check_perfect; // 전원 나갔을 때를 대비 

		Random rand = new Random();
		ResultSet rset;
		try {
			while (true) {
				System.out.println("어서오십시오. 작업을 선택해 주십시오.");
				System.out.println("1. 계정 생성");
				System.out.println("2. 계정 삭제");
				System.out.println("3. 계좌 생성");
				System.out.println("4. 계좌 삭제");
				System.out.println("5. 잔액 조회");
				System.out.println("6. 입금");
				System.out.println("7. 출금");
				System.out.print("your choice : ");
				String choice = Main.sc.nextLine();

				switch (choice){
				case "1":
					check_perfect = false;
					System.out.println("* 계정 생성 *");

					Calendar cal = Calendar.getInstance();
					start_day = cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DATE);				

					System.out.println(">> Type your password");
					password = Main.sc.nextLine();


					// 계정 번호 생성
					int year = cal.get(Calendar.YEAR) - 2000; // 18 얘는 계좌번호
					while(true) {
						//계좌번호생성acuid = Integer.toString(year)+"-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(8999)+1000);
						acuid = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
						System.out.println(acuid);
						//rset = Main.stmt.executeQuery("select * from user where UID='" + acuid + "'");
						rset = Main.stmt.executeQuery("select * from user");
						if(rset.next())
						{
							if(rset.getString("UID")==acuid)
							{
								System.out.println("있음");
							}
							else System.out.println("없음");
						}
						else System.out.println("에러어어어");
						break;
					}

					// db에서 계정 중복 있는지 확인해야함

					System.out.println(">> Type your name");
					name = Main.sc.nextLine();
					System.out.println(">> Type your address");
					address = Main.sc.nextLine();
					System.out.println(">> Type your phone number");
					phone = Main.sc.nextLine();
					System.out.println(">> Type your Birth Date (YYYY-MM-DD)");
					birth = Main.sc.nextLine();
					System.out.println(">> Select area you want to deposit");
					bnumber = Main.sc.nextLine();
					// 기본입력 끝
					// select 


					check_perfect = true;
					break;
				case "2":
					System.out.println("* 계정 삭제 *");

					// 계정 번호 입력
					// 비밀번호 입력
					// 확인
					// 삭제
					// 도큐먼트 수정

					break;
				case "3":
					System.out.println("* 계좌 생성 *");
					break;
				case "4":
					System.out.println("* 계좌 삭제 *");
					break;
				case "5":
					System.out.println("* 잔액 조회 *");
					break;
				case "6":
					System.out.println("* 입금 *");
					break;
				case "7":
					System.out.println("* 출금 *");
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}