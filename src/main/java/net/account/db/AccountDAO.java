package net.account.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

public class AccountDAO {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	public AccountDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/MysqlDB");
		} catch (NamingException e) {
			System.err.println("DB 설정 실패 : " + e);
			return;
		}
	}
	
	// 통장 번호 생성 메소드
	public String generateAccountNumber(String accountOffice, String accountType) {
	    Random random = new Random();
	    StringBuilder accountNumber = new StringBuilder();

	    String officeCode = "";
	    String typeCode = "";
	    
	    // 영업점 코드 - "Gangnam(강남)" -> "001", "Seocho(서초)" -> "002"
	    switch(accountOffice) {
	    case "Gangnam": officeCode = "001"; break;
	    case "Seocho": officeCode = "002"; break;
	    }
	    
	    // 통장 타입 코드 - "COMMON(기본)" -> "01", "PLUSBOX(플러스박스)" -> "02", "INSTALLMENT(적금)" -> "03"
	    switch(accountType) {
	    case "COMMON": typeCode = "01"; break;
	    case "PLUSBOX": typeCode = "02"; break;
	    case "INSTALLMENT": typeCode = "03"; break;
	    }

	    // 난수 - 6자리 숫자, 데이터베이스에 중복되지 않는 값으로 생성
	    long randomNumber;
	    String randomNumString;
	    do {
	        randomNumber = (long) (random.nextDouble() * 1_000_000L); // 최대 6자리 난수
	        randomNumString = String.format("%06d", randomNumber); // 6자리로 포맷팅
	    } while (isAccountNumberExists(officeCode + "-" + typeCode + "-" + randomNumString)); // 중복 체크

	    // 완성된 통장 번호 생성
	    accountNumber.append(officeCode).append("-").append(typeCode).append("-").append(randomNumString);

	    return accountNumber.toString();
	}

	// 데이터베이스에서 통장 번호가 이미 존재하는지 확인하는 메소드
	private boolean isAccountNumberExists(String accountNumber) {
	    String sql = "SELECT COUNT(*) FROM account WHERE account_num = ?";
	    try {
	        con = ds.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, accountNumber);

	        rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // 이미 존재하면 true 반환
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // 존재하지 않으면 false 반환
	}
	
	// 통장개설
    public boolean createAccount(AccountBean account, HttpServletRequest request) {
        boolean result = false;

        String sql = "INSERT INTO account (account_num, account_password, account_type, account_amount, account_date, user_id) "
                     + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            
            // 계좌 비밀번호 암호화
            String hashedPassword = BCrypt.hashpw(String.valueOf(account.getAccount_password()), BCrypt.gensalt());

            // account_num, account_password, account_type, account_amount, account_date 설정
            System.out.println(account.getAccount_num());
            
            pstmt.setString(1, account.getAccount_num());
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, account.getAccount_type().name());
            pstmt.setLong(4, account.getAccount_amount());
            pstmt.setDate(5, java.sql.Date.valueOf(account.getAccount_date()));

            // 세션에서 현재 로그인한 사용자 ID를 가져와서 user_id로 설정
            String userId = (String) request.getSession().getAttribute("userId"); // 세션에서 userId를 가져옴
            pstmt.setString(6, userId); // user_id 값을 DB에 삽입

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                result = true; // 통장 개설 성공
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
