package dbank;

import java.security.Key;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Users {

	Random rand = new Random();
	ResultSet rset, rset_ac; // ac = account
	boolean same_account,choice_end;
	String acuid, password, enc_pw; // 계정 번호와 비밀번호
	String cmd, pwd, isDelete ; // command
	String start_day, Anumber, name, address, phone, birth, bnumber; // Anumber:계좌번호, birth:yyyy-mm-dd형식, bnumber:branch number
	String query, choice;
	int money;

	public String check_same(String temp, int num) { // 중복 확인
		try {
			Calendar cal1 = Calendar.getInstance();
			same_account = false;
			while(rset.next()) {
				if(num==1) {
					if(Objects.equals(rset.getString("UID"), temp)) {
						//System.out.println("같은 번호의 계정입니다. 다시 수행합니다."); 또는 같은번호의 문서라서 다시실행
						same_account = true;
						temp = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
						break;
					}
				}
				else if(num==2) { // document number change
					if(Objects.equals(rset.getString("Dnum"), temp)) {
						System.out.println("같은 번호의 문서입니다. 다시 수행합니다.");
						same_account = true;
						temp = (cal1.get(Calendar.YEAR)-2000)+"-"+(rand.nextInt(899)+100)+"-"+(rand.nextInt(89999)+10000);
						break;
					}
				}
				else if(num==3) { // account
					same_account = true;
					temp = (cal1.get(Calendar.YEAR) - 2000)+"-"+(rand.nextInt(89999)+10000)+"-"+(rand.nextInt(899)+100);
					break;
				}
				
			}
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	public static Key getAESKey() throws Exception { // AES 128 암호화
	    String iv;
	    Key keySpec;

	    String key = "2014004739123456"; // key : 제 학번 + 123456 
	    iv = key.substring(0, 16);
	    byte[] keyBytes = new byte[16];
	    byte[] b = key.getBytes("UTF-8");

	    int len = b.length;
	    if (len > keyBytes.length) {
	       len = keyBytes.length;
	    }

	    System.arraycopy(b, 0, keyBytes, 0, len);
	    keySpec = new SecretKeySpec(keyBytes, "AES");

	    return keySpec;
	}

	// AES 128 암호화
	public static String encAES(String data) throws Exception {
        Key key = getAESKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

	// AES 128 복호화
	public static String decAES(String encryptedData) throws Exception {
        Key key = getAESKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        return new String(decValue);
    }
	
	public void run() throws Exception {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
		Date currentTime = new Date ();
		String today = mSimpleDateFormat.format ( currentTime );
		choice_end = false;
		same_account = false;

		try {
			while (true && choice_end==false) {
				System.out.println("\n\n\n어서오십시오. 작업을 선택해 주십시오.");

				System.out.print("0. Exit\n1. Manager\n2. Customer Menu\nInput : ");
				choice = Main.sc.nextLine();
				switch(choice){
				case "0": // exit
					choice_end=true;
					break;

				case "1": // manager

					break;



				case "2": // customer
					System.out.println("0. 이전 메뉴로 되돌아가기");
					System.out.println("1. 계정 생성");
					System.out.println("2. 계정 삭제");
					System.out.println("3. 계좌 생성");
					System.out.println("4. 계좌 삭제");
					System.out.println("5. 계좌 정보");
					System.out.println("6. 입금");
					System.out.println("7. 출금");
					System.out.print("your choice : ");
					choice = Main.sc.nextLine();
					switch (choice){
					case "0":
						break;

					case "1":
						System.out.println("* 계정 생성 *");

						while(true){
							System.out.println("\n>> Type your password");
							password = Main.sc.nextLine();
							String regExp = "^(?=.*[0-9])(?=.*[a-z]).{10,20}$";
							if(!password.matches(regExp)){ // 일치하지 않을때
								System.out.println("비밀번호는 문자와 숫자를 포함해 10자 이상 20자 이하입니다.");
							}
							else break;
						}

						// 완료. 계정 번호 생성
						rset = Main.stmt.executeQuery("select * from user");
						acuid = "123-1111-111";
						// acuid = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
						do{
							// 원본 acuid = check_same(acuid,1);
							rset = Main.stmt.executeQuery("select * from user where UID='"+acuid+"'");
							if(rset.next() && !same_account){
								System.out.println("계정 번호 중복. 다시 할당 받습니다.");
								acuid = check_same(acuid,1);
							}
							else if(!same_account)System.out.println("같은 번호의 계정 없음. 확정");
						} while(same_account);
						// 계정 번호 확정


						System.out.print(">> Type your name : ");
						name = Main.sc.nextLine();
						System.out.print(">> Type your address : ");
						address = Main.sc.nextLine();
						System.out.print(">> Type your phone number (doesn't typing '-') : ");
						phone = Main.sc.nextLine();
						System.out.print(">> Type your RRN(주민등록번호) : ");
						birth = Main.sc.nextLine();
						rset = Main.stmt.executeQuery("select * from document where Dbirth='"+birth+"' and DUID IS NOT NULL");
						if(rset.next()){
							System.out.println("기존 계정이 존재합니다. 계정은 하나만 생성 가능합니다.");
							break;
						}

						System.out.println(">> Select area you want to deposit");
						System.out.println("1. 서울\n2. 대전\n3. 대구\n4. 부산\n5. 제주");
						System.out.print("your choice : ");
						bnumber = Main.sc.nextLine();

						// aes128 encdoing
						enc_pw = encAES(password);
						
						// 이게 원본. 아래꺼는 테스트용
						//query = String.format("insert into user values " + "('%s','%s','%s','%s','%s','%s','%s')",
						//		phone, name, enc_pw, address,birth,bnumber,acuid);
						String enc_test = encAES("showshow12!");
						query = "insert into user values ('01019602111','test','"+enc_test+"','address test 123','661111-1111111','1','123-1111-111')";
						Main.stmt.executeUpdate(query); // user insert 완료

						// dnum = document number
						String dnum = (cal.get(Calendar.YEAR)-2000)+"-"+(rand.nextInt(899)+100)+"-"+(rand.nextInt(89999)+10000);
						do{
							dnum = check_same(dnum,2);
						} while(same_account);
						String dmgr = "18000" + bnumber;

						// 이게 원본, 아래꺼는 테스트용
						//query = String.format("insert into document values ('%s','%s','%s','%s','%s','%s','%s','%s',1,null,'%s')",
						//		today,
						//		name, address, phone, birth, bnumber, dnum, dmgr, acuid);

						// 생성 후 삭제 후 생성할때 지금은 에러떠도됨. 예시로 하느라 쿼리 때려박았으니
						query = "insert into document values ('2018-11-12','test','address test 123','01019602111','661111-1111111',1,'18-123-12312','180001',true,null,'123-1111-111')";
						Main.stmt.executeUpdate(query);
						break;

					case "2":
						System.out.println("* 계정 삭제 *");

						// 계정 번호 입력
						// 비밀번호 입력
						// 확인
						// 삭제
						// 도큐먼트 수정
						System.out.print("계정 번호 : ");
						cmd=Main.sc.nextLine();

						rset = Main.stmt.executeQuery("select * from user where UID='" + cmd + "'");

						if(rset.next())
						{
							System.out.print("비밀번호 : ");
							
							pwd=Main.sc.nextLine();
							enc_pw = encAES(pwd);
							if(enc_pw.equals(rset.getString("Apassword")))
							{
								System.out.println("* 계좌 정보 *");
								System.out.printf("%s\t %s\n",
										rset.getString("name"), rset.getString("UID"));
								System.out.println("삭제하시겠습니까? yes:1 , no:2");
								isDelete = Main.sc.nextLine();
								if(isDelete.equals("1"))
								{
									// document 수정
									//if(Main.stmt.executeUpdate("update * document set Storage='0',destruction='"+ today +" where UID='" + cmd + "'")!=0) {
									if(Main.stmt.executeUpdate("update document set Storage='0',destruction='"+ today +"' where DUID='123-1111-111'")!=0) {
										System.out.println("개인정보는 10년 후 파기됩니다.");
									}
									//test
									else System.out.println("문서 수정 실패하였습니다. [err21]");
									//if(Main.stmt.executeUpdate("delete from user where UID='" + cmd + "'")!=0)
									if(Main.stmt.executeUpdate("delete from user where UID='123-1111-111'")!=0)
									{
										System.out.println("삭제 성공 !!");

										
										
									}
									else
									{
										System.out.println("삭제에 실패하였습니다. [err32]");
									}
								}
								else System.out.println("취소를 선택하셨습니다.");
							}
							else
							{
								System.out.println("비밀번호를 확인해주십시오.");
							}
						}
						else
						{
							System.out.println("일치하는 계좌가 없습니다");
							continue;
						}
						break;

					case "3":
						System.out.println("* 계좌 생성 *");
						// 18-41201-482 이런식
						System.out.print("계정 번호 : ");
						acuid=Main.sc.nextLine();
						rset = Main.stmt.executeQuery("select * from user where UID='" + acuid + "'");
						
						
						
						if(rset.next())
						{
							System.out.print("비밀번호 : ");
							pwd=Main.sc.nextLine();
							enc_pw=encAES(pwd);
							if(enc_pw.equals(rset.getString("Apassword")))
							{
								Anumber = (cal.get(Calendar.YEAR) - 2000)+"-"+(rand.nextInt(89999)+10000)+"-"+(rand.nextInt(899)+100);
								do{
									Anumber = check_same(Anumber,3);
								} while(same_account);
								
								// 원본 query = " insert into account select name, Apassword, 0, Anumber, UBnum, 0, 0, acuid from user where user.UID=acuid;"
								query = "insert into account select name, Apassword, 0, '18-11111-111', UBnum, 0, 0, '123-1111-111' from user where user.UID='123-1111-111';";
								Main.stmt.executeUpdate(query); // user insert 완료
								System.out.println("계좌가 생성되었습니다.\n당신의 계좌번호는 "+Anumber+" 입니다.");
							}
							else
							{
								System.out.println("비밀번호를 확인해주십시오.");
							}
						}
						else
						{
							System.out.println("일치하는 계좌가 없습니다");
							continue;
						}
						
						
						// update user u, account a set a.Uname=u.name, a.password=u.Apassword,
						// a.ABnum=u.UBnum, a.asset=어쩌고, a.Anumber=저쩌고, a.deposit=어쩌고, a.withdraw=저쩌고 where user.UID = account.ACUID;
						// insert into account values (u.name, u.Apassword, 0, Anumber, u.UBnum, 0, 0, acuid) from user as u;

						/*
						| Uname  | password   | asset   | Anumber      | ABnum | deposit | withdraw | ACUID        |
						+--------+------------+---------+--------------+-------+---------+----------+--------------+
						| ava    | diodp121!  | 1004200 | 18-14538-472 |     5 | 1004200 |        0 | 123-1438-472 |
						 */

						break;
					case "4":
						System.out.println("* 계좌 삭제 *");

						System.out.print("계좌 번호 : ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where Anumber='" + cmd + "'");

						if(rset_ac.next())
						{
							System.out.print("비밀번호 : ");
							
							pwd=Main.sc.nextLine();
							enc_pw = encAES(pwd);
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.println("* 계좌 정보 *");
								System.out.printf("%s\t %s\t %d\n",
										rset_ac.getString("Uname"), rset_ac.getString("Anumber"), rset_ac.getInt("asset"));
								System.out.println("삭제하시겠습니까? yes:1 , no:2");
								isDelete = Main.sc.nextLine();
								if(isDelete.equals("1"))
								{
									if(rset_ac.getInt("asset")>0)
									{
										System.out.println("돈을 모두 출금 후 삭제해주십시오.");
										break;
									}
									//if(Main.stmt.executeUpdate("delete from account where Anumber='" + cmd + "'")!=0)
									if(Main.stmt.executeUpdate("delete from account where Anumber='18-11111-111'")!=0)
									{
										System.out.println("삭제 성공 !!");
									}
									else
									{
										System.out.println("삭제에 실패하였습니다. [err33]");
									}
								}
								else System.out.println("취소를 선택하셨습니다.");
							}
							else
							{
								System.out.println("비밀번호를 확인해주십시오.");
							}
						}
						else
						{
							System.out.println("일치하는 계좌가 없습니다");
							continue;
						}
						break;

					case "5":
						System.out.println("* 계좌 정보 *");
						boolean IsExist=false;
						System.out.print("계정 번호 : ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where ACUID='" + cmd + "'");

						System.out.print("비밀번호 : ");
						pwd=Main.sc.nextLine();
						enc_pw=encAES(pwd);
							
						while(rset_ac.next())
						{
							IsExist = true;
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.println("* 계좌 및 잔액 정보 *");
								System.out.printf("%s\t %s\t %d\t %d\t %s\t %s\n",
										rset_ac.getString("Uname"), rset_ac.getString("Anumber"), rset_ac.getInt("asset"),
										rset_ac.getInt("ABnum"), rset_ac.getString("ACUID"), decAES(rset_ac.getString(password)));
							}
							else
							{
								System.out.println("비밀번호를 확인해주십시오.");
							}
						}
						if(!IsExist)
						{
							System.out.println("일치하는 계좌가 없습니다");
						}

						break;
					case "6":
						System.out.println("* 입금 *");
						System.out.print("계좌 번호 : ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where Anumber='" + cmd + "'");

						if(rset_ac.next())
						{
							System.out.print("비밀번호 : ");
							
							pwd=Main.sc.nextLine();
							enc_pw=encAES(pwd);
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.println("얼마를 입금 하시겠습니까?");
								money = Integer.parseInt(Main.sc.nextLine());
								if(money > 10000000) {
									System.out.println("한번에 가능한 입출력 금액은 천만원 이하입니다.");
									break;
								}
								System.out.println("처리중입니다...(실제로 돈 받는다 가정)");
								Main.stmt.executeUpdate("update account set asset = asset + "+money+", deposit = deposit + "+money+" where Anumber='"+cmd+"'");
								System.out.printf("처리 완료 되었습니다.\n%s\t %s\t %d\n",
										rset_ac.getString("Uname"), rset_ac.getString("Anumber"), rset_ac.getInt("asset")+money);
							}
							else
							{
								System.out.println("비밀번호를 확인해주십시오.");
							}
						}
						else
						{
							System.out.println("일치하는 계좌가 없습니다");
							continue;
						}

						break;

					case "7":
						System.out.println("* 출금 *");

						System.out.print("계좌 번호 : ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where Anumber='" + cmd + "'");

						if(rset_ac.next())
						{
							System.out.print("비밀번호 : ");
							
							pwd=Main.sc.nextLine();
							enc_pw=encAES(pwd);
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.println("얼마를 출금 하시겠습니까?");
								money = Integer.parseInt(Main.sc.nextLine());
								if(money > 10000000) {
									System.out.println("한번에 가능한 입출력 금액은 천만원 이하입니다.");
									break;
								}
								if(money > rset_ac.getInt("asset")){
									System.out.println("잔액이 부족합니다. 다시 확인해 주십시오.");
									break;
								}
								System.out.println("처리중입니다...(실제로 돈 준다 가정)");
								Main.stmt.executeUpdate("update account set asset = asset - "+money+", withdraw = withdraw + "+money+" where Anumber='"+cmd+"'");

								System.out.printf("처리 완료 되었습니다.\n%s\t %s\t %d\n",
										rset_ac.getString("Uname"), rset_ac.getString("Anumber"), rset_ac.getInt("asset")-money);
							}
							else
							{
								System.out.println("비밀번호를 확인해주십시오.");
							}
						}
						else
						{
							System.out.println("일치하는 계좌가 없습니다");
							continue;
						}

						break;
					}

					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}