package dbank;

import java.util.Calendar;

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
		int bnumber; // branch number (예시 주어주고 고르라고 해야함)
		Boolean check_perfect; // 전원 나갔을 때를 대비 
		while (true) {
			System.out.println("어서오십시오. 작업을 선택해 주십시오.");
			System.out.println("1. 계정 생성");
			System.out.println("2. 계정 삭제");
			System.out.println("3. 계좌 생성");
			System.out.println("4. 계좌 삭제");
			System.out.println("5. 잔액 조회");
			System.out.println("6. 입금");
			System.out.println("7. 출금");
			String choice = Main.sc.nextLine();

			switch (choice){
			case "1":
				check_perfect = false;
				System.out.println("* 계정 생성 *");
				System.out.println(">> Type your password");
				password = Main.sc.nextLine();
				start_day = "2018-11-30";
				// acuid = "18-1223-5123"
				
				Calendar cal = Calendar.getInstance();
		        int year = cal.get(Calendar.YEAR) - 2000; // 18
		        // 계정 번호 생성
		        // db에서 계정 중복 있는지 확인해야함
				
		        System.out.println(">> Type your name");
		        
		        
				check_perfect = true;
				break;
			case "2":
				System.out.println("* 계정 삭제 *");
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
	}
}