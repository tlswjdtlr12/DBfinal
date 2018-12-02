package dbank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Users {

	Random rand = new Random();
	ResultSet rset;
	boolean same_account;
	String acuid; // 계정 번호
	String cmd;
	String password;
	String start_day;
	String Anumber; // 계좌 번호
	String name;
	String address;
	String phone;
	String birth; // yyyy-mm-dd 형식
	String bnumber; // branch number (예시 주어주고 고르라고 해야함)
	String query;
	Boolean check_perfect; // 전원 나갔을 때를 대비 
	
	public String check_same(String temp, int num) { // 중복 확인
		try {
			Calendar cal1 = Calendar.getInstance();
			same_account = false;
			while(rset.next()) {
				if(num==1) {
					if(Objects.equals(rset.getString("UID"), temp)) {
						System.out.println("같은 번호의 계정입니다. 다시 수행합니다.");
						same_account = true;
						temp = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
						break;
					}
				}
				else if(num==2) {
					if(Objects.equals(rset.getString("Dnum"), temp) && num==2) {
						System.out.println("같은 번호의 문서입니다. 다시 수행합니다.");
						same_account = true;
						temp = (cal1.get(Calendar.YEAR)-2000)+"-"+(rand.nextInt(899)+100)+"-"+(rand.nextInt(89999)+10000);
						break;
					}
				}
			}
			if(same_account) return temp;
			System.out.println("확정");
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public void run() {


		Calendar cal = Calendar.getInstance();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date currentTime = new Date ();
		String today = mSimpleDateFormat.format ( currentTime );


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
					System.out.println("* 계정 생성 *");
					System.out.println(">> Type your password");
					password = Main.sc.nextLine();

					// 완료. 계정 번호 생성
					rset = Main.stmt.executeQuery("select * from user");
					acuid = "123-4742-957";
					// acuid = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
					do{
						acuid = check_same(acuid,1);
					} while(same_account);


					System.out.print(">> Type your name : ");
					name = Main.sc.nextLine();
					System.out.print(">> Type your address : ");
					address = Main.sc.nextLine();
					System.out.print(">> Type your phone number (doesn't typing '-') : ");
					phone = Main.sc.nextLine();
					System.out.print(">> Type your Birth Date (YYYY-MM-DD) : ");
					birth = Main.sc.nextLine();
					System.out.println(">> Select area you want to deposit");
					System.out.println("1. 서울\n2. 대전\n3. 대구\n4. 부산\n5. 제주");
					System.out.print("your choice : ");
					bnumber = Main.sc.nextLine();

					
					// 이게 원본. 아래꺼는 테스트용
					//query = String.format("insert into user values " + "('%s','%s','%s','%s','%s','%s','%s')",
					//		phone, name, password, address,birth,bnumber,acuid);
					query = "insert into user values ('01019602111','test','showshow12!','address test 123','1966-11-11','1','123-1111-111')";
					Main.stmt.executeUpdate(query); // user insert 완료

					
					String dnum = (cal.get(Calendar.YEAR)-2000)+"-"+(rand.nextInt(899)+100)+"-"+(rand.nextInt(89999)+10000);
					do{
						dnum = check_same(dnum,2);
					} while(same_account);
					String dmgr = "18000" + bnumber;
					
					
					// 이게 원본, 아래꺼는 테스트용
					//query = String.format("insert into document values ('%s','%s','%s','%s','%s','%s','%s','%s',1,null)",
					//		today,
					//		name, address, phone, birth, bnumber, dnum, dmgr);
					query = "insert into document values ('2018-11-12','testtesttest','address test 123','01019602111','1966-11-11',1,'18-123-12312','180001',true,null,'123-1111-111')";
					Main.stmt.executeUpdate(query);
					break;
					
				case "2":
					System.out.println("* 계정 삭제 *");

					// 계정 번호 입력
					// 비밀번호 입력
					// 확인
					// 삭제
					// 도큐먼트 수정
					System.out.println("=====계좌 삭제=====");
					System.out.print("계좌 번호 : ");
					cmd=Main.sc.nextLine();

					rset = Main.stmt.executeQuery("select * from user where UID='" + cmd + "'");

					if(rset.next())
					{
						System.out.print("비밀번호 : ");
						String pwd;
						String isDelete;
						pwd=Main.sc.nextLine();
						if(pwd.equals(rset.getString("Apassword")))
						{
							System.out.println("* 계좌 정보 *");
							System.out.printf("%s\t %s\n",
									rset.getString("name"), rset.getString("UID"));
							System.out.println("삭제하시겠습니까? yes:1 , no:2");
							isDelete = Main.sc.nextLine();
							if(isDelete.equals("1"))
							{
								//if(Main.stmt.executeUpdate("delete from user where UID='" + cmd + "'")!=0)
								if(Main.stmt.executeUpdate("delete from user where UID='123-1111-111'")!=0)
								{
									System.out.println("삭제 성공 !!");
									// document 수정
									//if(Main.stmt.executeUpdate("update * document set Storage='0',destruction='"+ today +" where UID='" + cmd + "'")!=0) {
									if(Main.stmt.executeUpdate("update document set Storage='0',destruction='"+ today +"' where DUID='123-1111-111'")!=0) {
										System.out.println("개인정보는 1년 후 파기됩니다.");
									}
									//test
									else System.out.println("문서 수정 실패");
								}
								else
								{
									System.out.println("삭제에 실패");
								}
							}
							else System.out.println("취소를 선택하셨습니다.");
						}
						else
						{
							System.out.println("비밀번호가 틀렸습니다");
						}
					}
					else
					{
						System.out.println("해당 계좌번호에 맞는 계좌가 없습니다");
						continue;
					}
					break;
					
				case "3":
					System.out.println("* 계좌 생성 *");


					int year = cal.get(Calendar.YEAR) - 2000; // 18 얘는 계좌번호




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