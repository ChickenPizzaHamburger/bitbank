package net.lock.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class LockDAO {
    private DataSource ds;

    public LockDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/MysqlDB");
        } catch (NamingException e) {
            throw new RuntimeException("DB 설정 실패: " + e);
        }
    }

    public void handleLockExpiration(LockBean lock) throws SQLException {
        String updateLockSQL = "UPDATE `lock` SET is_active = false WHERE lock_id = ?";
        String refundSQL = "UPDATE account SET account_amount = account_amount + ? WHERE account_num = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement updateLockStmt = con.prepareStatement(updateLockSQL);
             PreparedStatement refundStmt = con.prepareStatement(refundSQL)) {

            con.setAutoCommit(false);

            // 락 상태 비활성화
            updateLockStmt.setLong(1, lock.getLockId());
            updateLockStmt.executeUpdate();

            // 원금 반환
            refundStmt.setLong(1, lock.getLockedAmount());
            refundStmt.setString(2, lock.getAccountNum());
            refundStmt.executeUpdate();

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }


    public List<LockBean> getLocksByAccount(String accountNum) throws SQLException {
        List<LockBean> locks = new ArrayList<>();
        String querySQL = "SELECT * FROM `lock` WHERE account_num = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL)) {

            stmt.setString(1, accountNum);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LockBean lock = new LockBean();
                    lock.setLockId(rs.getLong("lock_id"));
                    lock.setAccountNum(rs.getString("account_num"));
                    lock.setLockedAmount(rs.getLong("locked_amount"));
                    lock.setLockStartDate(rs.getTimestamp("lock_start_date"));
                    lock.setLockEndDate(rs.getTimestamp("lock_end_date"));
                    lock.setActive(rs.getBoolean("is_active"));
                    locks.add(lock);
                }
            }
        }
        return locks;
    }

    public void createLock(LockBean lock) throws SQLException {
        String insertSQL = "INSERT INTO `lock` (account_num, locked_amount, lock_start_date, lock_end_date, is_active) VALUES (?, ?, ?, ?, ?)";
        String updateSQL = "UPDATE account SET account_amount = account_amount - ? WHERE account_num = ?";  // 계좌에서 락 금액 차감

        try (Connection con = ds.getConnection();
             PreparedStatement insertStmt = con.prepareStatement(insertSQL);
             PreparedStatement updateStmt = con.prepareStatement(updateSQL)) {

            // 계좌에 활성화된 락이 있는지 확인
            String checkActiveLockSQL = "SELECT * FROM `lock` WHERE account_num = ? AND is_active = TRUE";
            try (PreparedStatement checkActiveLockStmt = con.prepareStatement(checkActiveLockSQL)) {
                checkActiveLockStmt.setString(1, lock.getAccountNum());
                ResultSet rs = checkActiveLockStmt.executeQuery();
                if (rs.next()) {
                    throw new SQLException("이미 활성화된 락이 존재합니다.");
                }
            }

            // 계좌 잔액 확인
            String checkBalanceSQL = "SELECT account_amount FROM account WHERE account_num = ?";
            try (PreparedStatement checkBalanceStmt = con.prepareStatement(checkBalanceSQL)) {
                checkBalanceStmt.setString(1, lock.getAccountNum());
                ResultSet rs = checkBalanceStmt.executeQuery();
                if (rs.next()) {
                    long currentBalance = rs.getLong("account_amount");
                    if (currentBalance < lock.getLockedAmount()) {
                        throw new SQLException("잔액이 부족합니다.");
                    }
                } else {
                    throw new SQLException("계좌를 찾을 수 없습니다.");
                }
            }

            // 트랜잭션 시작
            con.setAutoCommit(false);

            // 계좌에서 락 금액 차감
            updateStmt.setLong(1, lock.getLockedAmount());
            updateStmt.setString(2, lock.getAccountNum());
            updateStmt.executeUpdate();

            // 락 정보 삽입
            insertStmt.setString(1, lock.getAccountNum());
            insertStmt.setLong(2, lock.getLockedAmount());
            insertStmt.setTimestamp(3, lock.getLockStartDate());
            insertStmt.setTimestamp(4, lock.getLockEndDate());
            insertStmt.setBoolean(5, lock.isActive());
            insertStmt.executeUpdate();

            // 트랜잭션 커밋
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  // 예외 발생 시 트랜잭션 롤백
        }
    }


    public List<LockBean> getAllLocks() throws SQLException {
        List<LockBean> locks = new ArrayList<>();
        String querySQL = "SELECT * FROM `lock`"; // 모든 락 정보를 조회하는 SQL 쿼리

        try (Connection con = ds.getConnection();
             PreparedStatement stmt = con.prepareStatement(querySQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                LockBean lock = new LockBean();
                lock.setLockId(rs.getLong("lock_id"));
                lock.setAccountNum(rs.getString("account_num"));
                lock.setLockedAmount(rs.getLong("locked_amount"));
                lock.setLockStartDate(rs.getTimestamp("lock_start_date"));
                lock.setLockEndDate(rs.getTimestamp("lock_end_date"));
                lock.setActive(rs.getBoolean("is_active"));
                locks.add(lock); // List에 락 추가
            }
        }
        return locks; // 모든 락 정보 반환
    }

    public void updateExpiredLocks() throws SQLException {
        String updateLockSQL = "UPDATE `lock` SET is_active = FALSE WHERE lock_end_date <= NOW() AND is_active = TRUE";
        String restoreFundsSQL = "UPDATE account SET account_amount = account_amount + ? WHERE account_num = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement updateLockStmt = con.prepareStatement(updateLockSQL)) {

            // 만료된 락들을 비활성화합니다.
            con.setAutoCommit(false);

            // 락을 비활성화
            updateLockStmt.executeUpdate();

            // 만료된 락에 대해 금액을 복원
            String getExpiredLocksSQL = "SELECT * FROM `lock` WHERE lock_end_date <= NOW() AND is_active = FALSE";
            try (PreparedStatement getExpiredLocksStmt = con.prepareStatement(getExpiredLocksSQL);
                 ResultSet rs = getExpiredLocksStmt.executeQuery()) {

                while (rs.next()) {
                    long lockedAmount = rs.getLong("locked_amount");
                    String accountNum = rs.getString("account_num");

                    // 원래 계좌로 금액 복원
                    try (PreparedStatement restoreStmt = con.prepareStatement(restoreFundsSQL)) {
                        restoreStmt.setLong(1, lockedAmount);
                        restoreStmt.setString(2, accountNum);
                        restoreStmt.executeUpdate();
                    }
                }
            }

            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void testConnection() {
        try (Connection con = ds.getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed!");
        }
    }
}
