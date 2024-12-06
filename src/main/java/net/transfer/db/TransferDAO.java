package net.transfer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

public class TransferDAO {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	public TransferDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/MysqlDB");
		} catch (NamingException e) {
			System.err.println("DB 설정 실패 : " + e);
			return;
		}
	}
	
	// 사용자 로그인 검증 (아이디와 비밀번호 확인)
    public boolean isValidLogin(String userId, String password) {
        String sql = "SELECT 1 FROM users WHERE id = ? AND password = ?";
        boolean isValid = false;

        try {
        	con = ds.getConnection();
        	
	        pstmt = con.prepareStatement(sql);
	        
        	
            pstmt.setString(1, userId);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();

            if (rs.next()) {
               isValid = true;
            }
        } catch (SQLException e) {
            System.err.println("로그인 검증 실패 : " + e);
        } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            System.err.println("로그인 검증 후 DB 종료 실패 : " + e);
	        }
        }
        return isValid;
    }
	
	// 사용자 ID 목록 조회
	public List<String> getAllUserIds() {
	    String sql = "SELECT id FROM users";
	    List<String> userIdList = new ArrayList<>();

	    try {
	        con = ds.getConnection();
	        pstmt = con.prepareStatement(sql);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            userIdList.add(rs.getString("id"));
	        }
	    } catch (SQLException e) {
	        System.err.println("사용자 ID 목록 조회 실패 : " + e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            System.err.println("사용자 ID 목록 조회 후 DB 종료 실패 : " + e);
	        }
	    }
	    return userIdList;
	}
	
	// 사용자 ID를 기준으로 송금자와 받는 사람이 동일한 사용자인지 확인
	public boolean isSameUser(String senderAccount, String receiverAccount) {
	    String sql = "SELECT u1.user_id AS sender_user, u2.user_id AS receiver_user "
	               + "FROM account a1, account a2 "
	               + "JOIN users u1 ON a1.user_id = u1.id "
	               + "JOIN users u2 ON a2.user_id = u2.id "
	               + "WHERE a1.account_num = ? AND a2.account_num = ?";

	    boolean isSameUser = false;

	    try {
	        con = ds.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, senderAccount);
	        pstmt.setString(2, receiverAccount);
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String senderUserId = rs.getString("sender_user");
	            String receiverUserId = rs.getString("receiver_user");
	            isSameUser = senderUserId.equals(receiverUserId); // 송금자와 받는 사람이 동일한 사용자라면 true
	        }
	    } catch (SQLException e) {
	        System.err.println("사용자 체크 실패 : " + e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            System.err.println("사용자 체크 후 DB 종료 실패 : " + e);
	        }
	    }
	    return isSameUser;
	}
    
    // 비밀번호 체크
 	public boolean isPassword(String accountNum, String rawPassword) {
 	    String sql = "SELECT account_password FROM account WHERE account_num = ?";
 	    boolean isValid = false;

 	    try {
 	        con = ds.getConnection();
 	        pstmt = con.prepareStatement(sql);
 	        pstmt.setString(1, accountNum);

 	        rs = pstmt.executeQuery();

 	        if (rs.next()) {
 	            String hashedPassword = rs.getString("account_password");
 	            isValid = BCrypt.checkpw(rawPassword, hashedPassword); // 암호화 검증
 	        }
 	    } catch (SQLException e) {
 	        System.err.println("비밀번호 확인 실패 : " + e);
 	    } finally {
 	        try {
 	            if (rs != null) rs.close();
 	            if (pstmt != null) pstmt.close();
 	            if (con != null) con.close();
 	        } catch (SQLException e) {
 	            System.err.println("비밀번호 확인 후 DB 종료 실패 : " + e);
 	        }
 	    }

 	    return isValid;
 	}
    
	// 계좌번호 체크 - 보내기 전 체크
	public boolean isAccountNum(String accountNum) {
		String sql = "SELECT 1 FROM account WHERE account_num = ?";
		boolean isAccount = false;
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, accountNum);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				isAccount = true;
			}
		} catch (SQLException e) {
			System.err.println("계좌번호 체크 실패 : " + e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				System.err.println("계좌번호 체크 후 DB 종료 실패 : " + e);
			}
		}
		return isAccount;
	}
	
	// 사용자 체크 (받는 사람의 권한 판별) - 보낼 때 체크
	public boolean isRole(String id) {
		String sql = "SELECT role FROM users WHERE id = ?";
		boolean isRoot = false;
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				String role = rs.getString("role");
				isRoot = "root".equalsIgnoreCase(role); // 대소문자 구분X
			}
		} catch (SQLException e) {
			System.err.println("권한 체크 실패 : " + e);
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				System.err.println("권한 체크 후 DB 종료 실패 : " + e);
			}
		}
		return isRoot;
	}
	
	// 잔액 조회
	public long getAccountAmount(String accountNum) {
	    String sql = "SELECT account_amount FROM account WHERE account_num = ?";
	    long accountAmount = 0;

	    try {
	        con = ds.getConnection();
	        pstmt = con.prepareStatement(sql);
	        pstmt.setString(1, accountNum);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            accountAmount = rs.getLong("account_amount");
	        }
	    } catch (SQLException e) {
	        System.err.println("계좌 잔액 조회 실패 : " + e);
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            System.err.println("계좌 잔액 조회 후 DB 종료 실패 : " + e);
	        }
	    }

	    return accountAmount;
	}
		
	// 계좌 조회
	public List<Map<String, Object>> getAccountsById(String userId){
		List<Map<String, Object>> accountDetails = new ArrayList<>();
		String sql = "SELECT account_num, account_type, account_amount FROM account WHERE user_id = ?";
		
		try {
			con = ds.getConnection();
			
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
			    Map<String, Object> account = new HashMap<>();
			    account.put("accountNumber", rs.getString("account_num"));
			    account.put("accountType", rs.getString("account_type"));
			    account.put("accountAmount", rs.getLong("account_amount"));
			    accountDetails.add(account);
			}
		} catch (SQLException e) {
			System.err.println("계좌 조회 실패 : " + e);
		} finally {
	        try {
	            if (rs != null) rs.close();
	            if (pstmt != null) pstmt.close();
	            if (con != null) con.close();
	        } catch (SQLException e) {
	            System.err.println("계좌 조회 후 DB 종료 실패 : " + e);
	        }
	    }
		return accountDetails;
	}
	
	// 송금 기능
	public boolean transferMoney 
	(String senderAccount, String receiverAccount, long amount, String tag) {
		String transferSQL = "INSERT INTO transfer (sender_account, receiver_account, send_time, send_amount, tag)"
				+ "VALUES (?, ?, NOW(), ?, ?)";
		String senderSQL = "UPDATE account SET account_amount = account_amount - ? WHERE account_num = ?"; // 보내는 이 빼기
		String receiverSQL = "UPDATE account SET account_amount = account_amount + ? WHERE account_num = ?"; // 받는 이 더하기
		
		boolean success = false;
		
		try {
			con = ds.getConnection();
			con.setAutoCommit(false); // 쿼리문 하나하나 개별로 관리 (트랜잭션 수동)
			
			// 송금 정보
			pstmt = con.prepareStatement(transferSQL);
			pstmt.setString(1, senderAccount);
			pstmt.setString(2, receiverAccount);
			pstmt.setLong(3, amount);
			pstmt.setString(4, tag);
			pstmt.executeUpdate();
			
			// 보내는 사람 계좌 금액 감소
			pstmt = con.prepareStatement(senderSQL);
			pstmt.setLong(1, amount);
			pstmt.setString(2, senderAccount);
			pstmt.executeUpdate();
			
			// 받는 사람 계좌 금액 증가
			pstmt = con.prepareStatement(receiverSQL);
			pstmt.setLong(1, amount);
			pstmt.setString(2, receiverAccount);
			pstmt.executeUpdate();
			
			// 작업 성공시 커밋(쿼리문 보냄)
			con.commit();
			success = true;
		} catch (SQLException e) {
			try {
				if (con != null) con.rollback(); // 오류시 롤백
			} catch (SQLException ex) {
				System.err.println("롤백 실패 : " + ex);
			} System.err.println("송금 실패 : " + e);
		} finally {
			try {
				if (pstmt != null) pstmt.close();
				if (con != null) con.close();
			} catch (SQLException e) {
				System.err.println("송금 후 DB 종료 실패 : " + e);
			}
		}
		return success;
	}	
}
