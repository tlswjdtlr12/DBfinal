package dbank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Users {

	Random rand = new Random();
	ResultSet rset, rset_ac, rset_m; // ac = account, m = manager
	boolean same_account,choice_end;
	String acuid, password, enc_pw, dec_pw; // 계정 번호와 비밀번호
	String cmd, pwd, isDelete ; // command
	String start_day, Anumber, name, address, phone, birth, bnumber; // Anumber:계좌번호, birth:yyyy-mm-dd형식, bnumber:branch number
	String query, choice;
	int money;

	public void lines(int a) {
		for(int i=0;i < a;i++) System.out.print("-");
		System.out.println();
	}
	
	public String check_same(String temp, int num) { // 중복 확인
		try {
			Calendar cal1 = Calendar.getInstance();
			same_account = false;
			while(rset.next()) {
				if(num==1) {
					if(Objects.equals(rset.getString("UID"), temp)) { // user account
						same_account = true;
						temp = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
						break;
					}
				}
				else if(num==2) { // document number
					if(Objects.equals(rset.getString("Dnum"), temp)) {
						System.out.println("같은 번호의 문서입니다. 다시 수행합니다.");
						same_account = true;
						temp = (cal1.get(Calendar.YEAR)-2000)+"-"+(rand.nextInt(899)+100)+"-"+(rand.nextInt(89999)+10000);
						break;
					}
				}
				else if(num==3) { // bank account
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
		Key keySpec;

		String key = "2014004739123456"; // key : 제 학번 + 123456 
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
				System.out.println("\n어서오십시오. 작업을 선택해 주십시오.");

				System.out.print("0. Exit\n1. Manager\n2. Customer Menu\n>> ");
				choice = Main.sc.nextLine();
				switch(choice){
				case "0": // exit
					choice_end=true;
					System.out.println("\n수고 많으셨습니다. ^^");
					break;

				case "1": // manager
					// manager_pw = encAES("showshow11!, showshow22!, showshow33!, showshow44!, showshow55!");

					System.out.print("계정 번호 : ");
					cmd=Main.sc.nextLine();
					rset = Main.stmt.executeQuery("select * from manager where MID='" + cmd + "'");
					if(rset.next())
					{
						System.out.print("비밀번호 : ");

						pwd=Main.sc.nextLine();
						enc_pw = encAES(pwd);
						if(enc_pw.equals(rset.getString("mpassword")))
						{
							System.out.printf("\n어서오십시오. %s님\n", rset.getString("Mname"));
							boolean m_end=false;
							while(m_end==false) {
								System.out.println("\n0. 이전 메뉴로 되돌아가기");
								System.out.println("1. 지점 정보 확인");
								System.out.println("2. 계정 확인");
								System.out.println("3. 파기 계정 확인");
								System.out.print("your choice : ");
								choice = Main.sc.nextLine();
								switch (choice){
								case "0":
									m_end=true;
									break;

								case "1":
									System.out.println("\n* 지점 정보 확인 *");
									rset_m = Main.stmt.executeQuery("select * from branch where Bmgr="+rset.getString("Mid"));
									if(rset_m.next()) {
										System.out.printf("%-10s | %-9s | %-12s | %-6s | %-5s | %-7s\n",
												"Basset", "Bname", "Btelnum", "Barea", "Bnum", "Bmgr");
										lines(64);
										System.out.printf("%-10d | %-9s | %-12s | %-6s | %-5s | %-7s\n",
												rset_m.getInt("Basset"),rset_m.getString("Bname"),rset_m.getString("Btelnum"),rset_m.getString("Barea"),rset_m.getInt("Bnum"),rset_m.getString("Bmgr"));
									}
									else System.out.println("error[63]");
									break;

								case "2":
									System.out.println("\n* 계정 확인 *");
									rset_m = Main.stmt.executeQuery("select * from account where ABnum='"+rset.getString("Mid").substring(5,6)+"'"); // 마지막 한글자 추출
									boolean check_acc=false;
									System.out.printf("%-10s | %-10s | %-13s | %-6s | %-13s\n",
											"Uname", "asset", "Anumber", "ABnum", "ACUID");
									lines(64);
									while(rset_m.next()) {
										check_acc=true;
										System.out.printf("%-10s | %-10s | %-13s | %-6s | %-13s\n",
												rset_m.getString("Uname"),rset_m.getInt("asset"),rset_m.getString("Anumber"),rset_m.getInt("ABnum"),rset_m.getString("ACUID"));
									}
									if(check_acc=false) System.out.println("지점에 계정이 존재하지 않습니다.");

									break;

								case "3":
									System.out.println("\n* 파기 계정 확인 *");
									SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									rset_m = Main.stmt.executeQuery("select * from document where Dbranch='"+rset.getString("Mid")+"'");
									List<Object> oPerlishArray = new ArrayList<Object>(); // 동적할당
									System.out.printf("%-10s | %-9s | %-12s | %-6s | %-5s | %-7s\n",
											"Basset", "Bname", "Btelnum", "Barea", "Bnum", "Bmgr");
									lines(64);
									while(rset_m.next()) {
										String start = rset_m.getString("destruction");
										String end = today;

										Date beginDate = formatter.parse(start);
										Date endDate = formatter.parse(end);

										// 하루 단위
										long diff = endDate.getTime() - beginDate.getTime();
										long diffDays = diff / (24 * 60 * 60 * 1000);

										if(diffDays > 3650) { // 10년
											oPerlishArray.add(rset_m.getString("Dnum"));
											System.out.printf("%-10s | %-9s | %-12s | %-6s | %-5s | %-7s\n",
													rset_m.getInt("Basset"),rset_m.getString("Bname"),rset_m.getString("Btelnum"),rset_m.getString("Barea"),rset_m.getInt("Bnum"),rset_m.getString("Bmgr"));
										}
									}
									if(oPerlishArray.size() > 0) {
										System.out.print("계정들을 삭제하시겠습니까? yes:1 , no:2\n>> ");
										isDelete = Main.sc.nextLine();
										if(isDelete.equals("1"))
										{
											for(int i=0;i<oPerlishArray.size();i++) {
												// document 삭제
												if(Main.stmt.executeUpdate("delete from document where Dnum='"+oPerlishArray.get(i)+"'")!=0) {
													System.out.printf(oPerlishArray.get(i)+" 문서가 DB에서 삭제 되었습니다.");
												}
											}
										}
										else System.out.println("취소를 선택하셨습니다.");

									}
									else System.out.println("파기할 문서가 존재하지 않습니다.");
									break;
								}
							}
						}
						else
						{
							System.out.println("비밀번호를 확인해주십시오.");
							break;
						}
					}
					else
					{
						System.out.println("일치하는 계정이 없습니다");
						break;
					}
					break;

				case "2": // customer
					System.out.println("\n0. 이전 메뉴로 되돌아가기");
					System.out.println("1. 계정 생성");
					System.out.println("2. 계정 삭제");
					System.out.println("3. 계좌 생성");
					System.out.println("4. 계좌 삭제");
					System.out.println("5. 계좌 정보");
					System.out.println("6. 입금");
					System.out.println("7. 출금");
					System.out.print(">> ");
					choice = Main.sc.nextLine();
					switch (choice){
					case "0":
						break;

					case "1":
						System.out.println("\n* 계정 생성 *");

						rset = Main.stmt.executeQuery("select * from user");
						acuid = "123-"+(rand.nextInt(8999)+1000)+"-"+(rand.nextInt(899)+100);
						do{
							acuid = check_same(acuid,1);
							rset = Main.stmt.executeQuery("select * from user where UID='"+acuid+"'");
							if(rset.next() && !same_account){
								System.out.println("계정 번호 중복. 다시 할당 받습니다.");
								acuid = check_same(acuid,1);
							}
							else if(!same_account)System.out.println("계정 번호 확정");
						} while(same_account);
						// 계정 번호 확정

						while(true){
							System.out.print("Type your password >> ");
							password = Main.sc.nextLine();
							String regExp = "^(?=.*[0-9])(?=.*[a-z]).{10,20}$";
							if(!password.matches(regExp)){ // 일치하지 않을때
								System.out.println("비밀번호는 문자와 숫자를 포함해 10자 이상 20자 이하입니다.");
							}
							else break;
						}
						System.out.print("Type your name >> ");
						name = Main.sc.nextLine();
						System.out.print("Type your address >> ");
						address = Main.sc.nextLine();
						System.out.print("Type your phone number (doesn't typing '-') >> ");
						phone = Main.sc.nextLine();
						System.out.print("Type your RRN(주민등록번호) >> ");
						birth = Main.sc.nextLine();
						rset = Main.stmt.executeQuery("select * from document where Dbirth='"+birth+"' and DUID IS NOT NULL");
						if(rset.next()){
							System.out.println("기존 계정이 존재합니다. 계정은 하나만 생성 가능합니다.");
							break;
						}

						System.out.println("Select area you want to deposit");
						System.out.println("1. 서울\n2. 대전\n3. 대구\n4. 부산\n5. 제주");
						System.out.print(">> ");
						bnumber = Main.sc.nextLine();
						enc_pw = encAES(password);
						
						query = String.format("insert into user values " + "('%s','%s','%s','%s','%s','%s','%s')",
								phone, name, enc_pw, address,birth,bnumber,acuid);
						Main.stmt.executeUpdate(query); // user insert 완료

						// document number create
						String dnum = (cal.get(Calendar.YEAR)-2000)+"-"+(rand.nextInt(899)+100)+"-"+(rand.nextInt(89999)+10000);
						do{
							dnum = check_same(dnum,2);
						} while(same_account);
						String dmgr = "18000" + bnumber;
						query = String.format("insert into document values ('%s','%s','%s','%s','%s','%s','%s','%s',true,null,'%s')",
								today, name, address, phone, birth, bnumber, dnum, dmgr, acuid);
						Main.stmt.executeUpdate(query);
						System.out.printf("환영합니다. 당신의 계정번호는 %s입니다. 계좌를 생성하여 주십시오.\n",acuid);
						break;

					case "2":
						System.out.println("\n* 계정 삭제 *");
						System.out.print("계정 번호 >> ");
						cmd=Main.sc.nextLine();
						rset = Main.stmt.executeQuery("select * from user where UID='" + cmd + "'");
						if(rset.next())
						{
							System.out.print("비밀번호 >> ");
							pwd=Main.sc.nextLine();
							enc_pw = encAES(pwd);
							if(enc_pw.equals(rset.getString("Apassword")))
							{
								System.out.println("\n* 계좌 정보 *");
								System.out.printf("%-10s | %-13s\n", "name", "UID");
								lines(26);
								System.out.printf("%-10s | %-13s\n",	rset.getString("name"), rset.getString("UID"));
								System.out.println("삭제하시겠습니까? yes:1 , no:2\n>> ");
								isDelete = Main.sc.nextLine();
								if(isDelete.equals("1"))
								{
									if(Main.stmt.executeUpdate("update document set Storage='0',destruction='"+ today +"' where DUID='" + cmd + "'")!=0) {
										System.out.println("개인정보는 10년 후 파기됩니다.");
									}
									else System.out.println("문서 수정 실패하였습니다. [err21]");
									if(Main.stmt.executeUpdate("delete from user where UID='" + cmd + "'")!=0)
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
						System.out.println("\n* 계좌 생성 *");
						System.out.print("계정 번호 >> ");
						acuid=Main.sc.nextLine();
						rset = Main.stmt.executeQuery("select * from user where UID='" + acuid + "'");
						if(rset.next())
						{
							System.out.print("비밀번호 >> ");
							pwd=Main.sc.nextLine();
							enc_pw=encAES(pwd);
							if(enc_pw.equals(rset.getString("Apassword")))
							{
								Anumber = (cal.get(Calendar.YEAR) - 2000)+"-"+(rand.nextInt(89999)+10000)+"-"+(rand.nextInt(899)+100);
								do{
									Anumber = check_same(Anumber,3);
								} while(same_account);

								query = "insert into account select name, Apassword, 0, '"+Anumber+"', UBnum, 0, 0, '"+acuid+"' from user where user.UID='"+acuid+"';";
								Main.stmt.executeUpdate(query); // user insert 완료
								System.out.println("\n계좌가 생성되었습니다.\n당신의 계좌번호는 "+Anumber+" 입니다.\n비밀번호는 계정의 비밀번호와 같습니다.");
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
						
					case "4":
						System.out.println("\n* 계좌 삭제 *");
						System.out.print("계좌 번호 >> ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where Anumber='" + cmd + "'");
						if(rset_ac.next())
						{
							System.out.print("비밀번호 >> ");
							pwd=Main.sc.nextLine();
							enc_pw = encAES(pwd);
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.println("\n* 계좌 정보 *");
								System.out.printf("%-10s | %-13s | %-10s\n", "Uname", "Anumber", "asset");
								lines(26);
								System.out.printf("%-10s | %-13s | %-10s\n",
										rset_ac.getString("Uname"), rset_ac.getString("Anumber"), rset_ac.getInt("asset"));
								System.out.print("삭제하시겠습니까? yes:1 , no:2\n>> ");
								isDelete = Main.sc.nextLine();
								if(isDelete.equals("1"))
								{
									if(rset_ac.getInt("asset")>0)
									{
										System.out.println("돈을 모두 출금 후 삭제해주십시오.");
										break;
									}
									if(Main.stmt.executeUpdate("delete from account where Anumber='" + cmd + "'")!=0)
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
						System.out.println("\n* 계좌 정보 *");
						boolean IsExist=false;
						System.out.print("계정 번호 >> ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where ACUID='" + cmd + "'");
						System.out.print("비밀번호 >> ");
						pwd=Main.sc.nextLine();
						enc_pw=encAES(pwd);
						System.out.println("\n* 계좌 및 잔액 정보 *");
						System.out.printf("%-10s | %-13s | %-10s | %-6s | %-13s | %-20s\n",
								"Uname", "Anumber", "asset", "ABnum", "ACUID", "password");
						lines(93);
						while(rset_ac.next())
						{
							IsExist = true;
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.printf("%-10s | %-13s | %-10s | %-6s | %-13s | %-20s\n",
										rset_ac.getString("Uname"), rset_ac.getString("Anumber"), rset_ac.getInt("asset"),
										rset_ac.getInt("ABnum"), rset_ac.getString("ACUID"), decAES(rset_ac.getString("password")));
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
						System.out.println("\n* 입금 *");
						System.out.print("계좌 번호 >> ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where Anumber='" + cmd + "'");

						if(rset_ac.next())
						{
							System.out.print("비밀번호 >> ");

							pwd=Main.sc.nextLine();
							enc_pw=encAES(pwd);
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.print("얼마를 입금 하시겠습니까?\n>> ");
								money = Integer.parseInt(Main.sc.nextLine());
								if(money > 10000000) {
									System.out.println("한번에 가능한 입출력 금액은 천만원 이하입니다.");
									break;
								}
								System.out.println("처리중입니다...(실제로 돈 받는다 가정)");
								Main.stmt.executeUpdate("update account set asset = asset + "+money+", deposit = deposit + "+money+" where Anumber='"+cmd+"'");
								Main.stmt.executeUpdate("update branch set Basset = Basset + "+money+" where Bnum="+rset_ac.getInt("ABnum"));
								System.out.printf("처리 완료 되었습니다. 잔액 : %d\n", rset_ac.getInt("asset")+money);
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
						System.out.println("\n* 출금 *");

						System.out.print("계좌 번호 >> ");
						cmd=Main.sc.nextLine();
						rset_ac = Main.stmt.executeQuery("select * from account where Anumber='" + cmd + "'");

						if(rset_ac.next())
						{
							System.out.print("비밀번호 >> ");

							pwd=Main.sc.nextLine();
							enc_pw=encAES(pwd);
							if(enc_pw.equals(rset_ac.getString("password")))
							{
								System.out.print("얼마를 출금 하시겠습니까?\n>> ");
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
								Main.stmt.executeUpdate("update branch set Basset = Basset - "+money+" where Bnum="+rset_ac.getInt("ABnum"));

								System.out.printf("처리 완료 되었습니다. 잔액 : %d\n", rset_ac.getInt("asset")-money);
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