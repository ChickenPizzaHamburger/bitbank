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
		String sql = "SELECT 1 FROM user WHERE id = ? AND password = ?";
		boolean isValid = false;

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			pstmt.setString(2, password);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				isValid = true;
			}
		} catch (SQLException e) {
			System.err.println("로그인 검증 실패 : " + e);
		}
		return isValid;
	}

	// 사용자 ID 목록 조회
	public List<String> getAllUserIds() {
		String sql = "SELECT id FROM user";
		List<String> userIdList = new ArrayList<>();

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			rs = pstmt.executeQuery();

			while (rs.next()) {
				userIdList.add(rs.getString("id"));
			}
		} catch (SQLException e) {
			System.err.println("사용자 ID 목록 조회 실패 : " + e);
		}
		return userIdList;
	}

	// 사용자 ID를 기준으로 송금자와 받는 사람이 동일한 사용자인지 확인
	public boolean isSameUser(String senderAccount, String receiverAccount) {
		String sql = "SELECT a1.user_id AS sender_user, a2.user_id AS receiver_user " + "FROM account a1 "
				+ "JOIN account a2 ON a1.user_id = a2.user_id " + "WHERE a1.account_num = ? AND a2.account_num = ?";

		boolean isSameUser = false;

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
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
		}
		return isSameUser;
	}

	// 비밀번호 체크
	public boolean isPassword(String accountNum, String rawPassword) {
		String sql = "SELECT account_password FROM account WHERE account_num = ?";
		boolean isValid = false;

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, accountNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String hashedPassword = rs.getString("account_password");
				isValid = BCrypt.checkpw(rawPassword, hashedPassword); // 암호화 검증
			}
		} catch (SQLException e) {
			System.err.println("비밀번호 확인 실패 : " + e);
		}
		return isValid;
	}

	// 계좌번호 체크 - 보내기 전 체크
	public boolean isAccountNum(String accountNum) {
		String sql = "SELECT 1 FROM account WHERE account_num = ?";
		boolean isAccount = false;

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, accountNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				isAccount = true;
			}
		} catch (SQLException e) {
			System.err.println("계좌번호 체크 실패 : " + e);
		}
		return isAccount;
	}

	// 사용자 체크 (받는 사람의 권한 판별) - 보낼 때 체크
	public boolean isRole(String id) {
		String sql = "SELECT role FROM user WHERE id = ?";
		boolean isRoot = false;

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String role = rs.getString("role");
				isRoot = "root".equalsIgnoreCase(role); // 대소문자 구분X
			}
		} catch (SQLException e) {
			System.err.println("권한 체크 실패 : " + e);
		}
		return isRoot;
	}

	// 잔액 조회
	public long getAccountAmount(String accountNum) {
		String sql = "SELECT account_amount FROM account WHERE account_num = ?";
		long accountAmount = 0;

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, accountNum);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				accountAmount = rs.getLong("account_amount");
			}
		} catch (SQLException e) {
			System.err.println("계좌 잔액 조회 실패 : " + e);
		}
		return accountAmount;
	}

	// 계좌 조회
	public List<Map<String, Object>> getAccountsById(String userId) {
		List<Map<String, Object>> accountDetails = new ArrayList<>();
		String sql = "SELECT account_num, account_type, account_amount FROM account WHERE user_id = ?";

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
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
		}
		return accountDetails;
	}
	
	private String findFinalSender(String currentSender, Connection con) {
	    if ("000-00-000000".equals(currentSender)) {
	        String sql = "SELECT sender_account FROM transfer WHERE receiver_account = '000-00-000000' ORDER BY send_time DESC LIMIT 1";
	        try (PreparedStatement pstmt = con.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("sender_account");
	            }
	        } catch (SQLException e) {
	            System.err.println("최종 송금자 조회 실패: " + e.getMessage());
	        }
	    }
	    return currentSender;
	}

	private String findFinalReceiver(String currentReceiver, Connection con) {
	    if ("000-00-000000".equals(currentReceiver)) {
	        String sql = "SELECT receiver_account FROM transfer WHERE sender_account = '000-00-000000' ORDER BY send_time DESC LIMIT 1";
	        try (PreparedStatement pstmt = con.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("receiver_account");
	            }
	        } catch (SQLException e) {
	            System.err.println("최종 수신자 조회 실패: " + e.getMessage());
	        }
	    }
	    return currentReceiver;
	}

	// 송금 내역 조회
	public List<Map<String, Object>> getTransferHistory(String accountNum) {
	    String sql = "SELECT sender_account, receiver_account, send_time, send_amount, tag "
	               + "FROM transfer WHERE sender_account = ? OR receiver_account = ? "
	               + "ORDER BY send_time DESC";
	    List<Map<String, Object>> transferHistory = new ArrayList<>();

	    try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
	        pstmt.setString(1, accountNum);
	        pstmt.setString(2, accountNum);

	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            // 원본 데이터
	            String senderAccount = rs.getString("sender_account");
	            String receiverAccount = rs.getString("receiver_account");
	            long sendAmount = rs.getLong("send_amount");
	            String sendTime = rs.getString("send_time");
	            String tag = rs.getString("tag");

	            // 루트 계좌를 경유한 경우 최종 송금자와 수신자를 확인
	            String finalSender = findFinalSender(senderAccount, con);
	            String finalReceiver = findFinalReceiver(receiverAccount, con);

	            // 디버깅 로그
	            System.out.println("최종 송금자 계좌: " + finalSender + ", 최종 수신자 계좌: " + finalReceiver);

	            // 거래 내역 추가
	            Map<String, Object> transfer = new HashMap<>();
	            transfer.put("senderAccount", finalSender);
	            transfer.put("receiverAccount", finalReceiver);
	            transfer.put("sendTime", sendTime);
	            transfer.put("sendAmount", sendAmount);
	            transfer.put("tag", tag);

	            transferHistory.add(transfer);
	        }
	    } catch (SQLException e) {
	        System.err.println("송금 내역 조회 실패 : " + e);
	    }

	    return transferHistory;
	}

	// 송금 기능
	public boolean transferMoney(String senderAccount, String receiverAccount, long amount, String tag) {
	    String transferSQL = "INSERT INTO transfer (sender_account, receiver_account, send_time, send_amount, tag)"
	            + " VALUES (?, ?, NOW(), ?, ?)";
	    String senderSQL = "UPDATE account SET account_amount = account_amount - ? WHERE account_num = ?"; // 송금자 계좌 금액 차감
	    String rootSQL = "UPDATE account SET account_amount = account_amount + ? WHERE account_num = ?"; // 루트 계좌 금액 증가
	    String receiverSQL = "UPDATE account SET account_amount = account_amount + ? WHERE account_num = ?"; // 받는 사람 계좌 금액 증가

	    boolean success = false;

	    try (Connection con = ds.getConnection()) {
	        con.setAutoCommit(false); // 수동 커밋 설정

	        try (PreparedStatement transferStmt = con.prepareStatement(transferSQL);
	             PreparedStatement senderStmt = con.prepareStatement(senderSQL);
	             PreparedStatement rootStmt = con.prepareStatement(rootSQL);
	             PreparedStatement receiverStmt = con.prepareStatement(receiverSQL)) {

	            // 송금 내역에 실제 입력된 송금자 계좌와 받는 사람 계좌 기록
	            transferStmt.setString(1, senderAccount);  // 송금자 계좌
	            transferStmt.setString(2, receiverAccount);  // 받는 사람 계좌
	            transferStmt.setLong(3, amount);
	            transferStmt.setString(4, tag);
	            transferStmt.executeUpdate();

	            // 송금자 계좌에서 금액 차감
	            senderStmt.setLong(1, amount);
	            senderStmt.setString(2, senderAccount);
	            senderStmt.executeUpdate();

	            // 루트 계좌로 금액을 추가할 경우만 처리
	            if ("000-00-000000".equals(senderAccount) || "000-00-000000".equals(receiverAccount)) {
	                rootStmt.setLong(1, amount);
	                rootStmt.setString(2, "000-00-000000");
	                rootStmt.executeUpdate();
	            }

	            // 받는 사람 계좌로 금액 추가
	            receiverStmt.setLong(1, amount);
	            receiverStmt.setString(2, receiverAccount);
	            receiverStmt.executeUpdate();

	            con.commit();
	            success = true;
	        } catch (SQLException e) {
	            con.rollback();
	            System.err.println("송금 실패: " + e.getMessage());
	        }

	    } catch (SQLException e) {
	        System.err.println("DB 연결 실패 또는 종료 실패: " + e.getMessage());
	    }
	    return success;
	}

	// num값으로 type과 amount 찾기
	public List<Map<String, Object>> getAccountDetailsByNum(String accountNum) {
		String sql = "SELECT account_type, account_amount FROM account WHERE account_num = ?";
		List<Map<String, Object>> accountDetailsList = new ArrayList<>();

		try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
			pstmt.setString(1, accountNum); // num 값으로 쿼리 파라미터 설정

			rs = pstmt.executeQuery();

			while (rs.next()) {
				Map<String, Object> accountDetails = new HashMap<>();
				accountDetails.put("accountType", rs.getString("account_type"));
				accountDetails.put("accountAmount", rs.getLong("account_amount"));
				accountDetailsList.add(accountDetails);
			}
		} catch (SQLException e) {
			System.err.println("계좌 정보 조회 실패 : " + e);
		}
		return accountDetailsList;
	}
}