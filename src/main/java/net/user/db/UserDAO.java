package net.user.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UserDAO {
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	DataSource ds;
	
	public UserDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/MysqlDB");
		} catch (NamingException e) {
			System.err.println("DB 설정 실패 : " + e);
			return;
		}
	}
	
	// 회원가입 메소드
    public boolean signup(UserBean user) {
        String sql = "INSERT INTO user (userId, username, password, email, birthDate, gender, role, createdAt) "
        		+ "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        boolean isRegistered = false;

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setObject(5, user.getBirthDate());
            pstmt.setString(6, user.getGender().name());
            pstmt.setString(7, user.getRole().name());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                isRegistered = true;
            }
        } catch (SQLException e) {
            System.err.println("회원가입 실패 : " + e);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("회원가입 후 DB 종료 실패 : " + e);
            }
        }
        return isRegistered;
    }
    
    // 회원 확인 메소드
    public boolean checkUserIdExists(String userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE userId = ?";
        boolean exists = false;

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                // 만약 사용자 ID가 존재하면, COUNT(*) 값이 1 이상이면 true
                int count = rs.getInt(1);
                if (count > 0) {
                    exists = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("아이디 존재 여부 확인 실패 : " + e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("아이디 존재 여부 확인 후 DB 종료 실패 : " + e);
            }
        }
        return exists;
    }
    
    // 사용자 비밀번호 확인 메소드
    public boolean checkPassword(String userId, String password) {
        String sql = "SELECT COUNT(*) FROM user WHERE userId = ? AND password = ?";
        boolean isValid = false;

        try {
            con = ds.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, password); // 입력한 비밀번호로 확인

            rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    isValid = true;
                }
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
    
    public String findId(String email, String birthdate) {
    	String sql = "SELECT userId FROM user WHERE email = ? AND birthDate = ?";
    	String id = "";
    	
    	try {
    		con = ds.getConnection();
    		pstmt = con.prepareStatement(sql);
    		pstmt.setString(1, email);
    		pstmt.setString(2, birthdate);
    		
    		rs = pstmt.executeQuery();
    		
    		if (rs.next()) {
    			id = rs.getString("userId");
    		}
    	} catch (SQLException e) {
    		System.err.println("아이디 찾기 실패 : " + e);
    	} finally {
    		try {
    			if (rs != null) rs.close();
        		if (pstmt != null) pstmt.close();
        		if (con != null) con.close();
    		} catch (SQLException e) {
    			System.err.println("아이디 찾은 후 DB 종료 실패 : " + e);
    		}
    	}
    	return id;
    }
}
