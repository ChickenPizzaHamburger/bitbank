package net.user.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {
    // 필드 선언
    DataSource ds;

    public UserDAO() {
        try {
            Context init = new InitialContext();
            ds = (DataSource) init.lookup("java:comp/env/jdbc/MysqlDB");
        } catch (NamingException e) {
            System.err.println("DB 설정 실패 : " + e);
        }
    }

    public void testConnection() {
        // DB 연결 테스트
        try (Connection con = ds.getConnection()) {
            if (con != null) {
                System.out.println("DB 연결 성공!");
            }
        } catch (SQLException e) {
            System.err.println("DB 연결 실패 : " + e.getMessage());
        }
    }
    
    // 회원가입 메소드
    public boolean signup(UserBean user) {
        String sql = "INSERT INTO user (userId, username, password, email, birthDate, gender, role, createdAt) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        boolean isRegistered = false;

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setObject(5, user.getBirthDate());
            pstmt.setString(6, user.getGender().name());
            pstmt.setString(7, user.getRole().name());
            
            System.out.println("DB Access > ");

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                isRegistered = true;
            }
        } catch (SQLException e) {
            System.err.println("회원가입 실패 : " + e);
        }
        return isRegistered;
    }

    // 회원 아이디 확인 (로그인)
    public boolean checkUserId(String userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE userId = ?";
        boolean exists = false;

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        exists = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("아이디 존재 여부 확인 실패 : " + e);
        }
        return exists;
    }

    // 회원 비밀번호 확인 (로그인)
    public boolean checkUserPassword(String userId, String inputPassword) {
        String sql = "SELECT password FROM user WHERE userId = ?";
        String storedPassword = null; // DB에 저장된 암호화된 비밀번호

        try (Connection con = ds.getConnection(); 
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    storedPassword = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println("비밀번호 확인 실패 : " + e);
            return false;
        }
        
        System.out.println("비교 : " + BCrypt.checkpw(inputPassword, storedPassword));

        // 암호화된 비밀번호와 입력된 비밀번호 비교
        if (storedPassword != null && BCrypt.checkpw(inputPassword, storedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    // 아이디 찾기 (이메일, 생년월일 확인)
    public String findId(String email, String birthdate) {
        String sql = "SELECT userId FROM user WHERE email = ? AND birthDate = ?";
        String id = "";

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, birthdate);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    id = rs.getString("userId");
                }
            }
        } catch (SQLException e) {
            System.err.println("아이디 찾기 실패 : " + e);
        }
        return id;
    }

    // 비밀번호 찾기 (아이디, 이메일 확인)
    public boolean checkUserEmail(String userId, String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE userId = ? AND email = ?";
        boolean isValid = false;

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setString(2, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        isValid = true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("아이디와 이메일 확인 실패 : " + e);
        }
        return isValid;
    }

    // 아이디 중복 체크
    public boolean isUserIdDuplicate(String userId) {
        String sql = "SELECT userId FROM user WHERE userId = ?";
        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();  // 결과가 있으면 중복
            }
        } catch (SQLException e) {
            System.err.println("아이디 중복 체크 실패 : " + e);
        }
        return false;
    }

    // 이메일 중복 체크
    public boolean isEmailDuplicate(String email) {
        String sql = "SELECT email FROM user WHERE email = ?";
        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();  // 결과가 있으면 중복
            }
        } catch (SQLException e) {
            System.err.println("이메일 중복 체크 실패 : " + e);
        }
        return false;
    }
    
    // 비밀번호 재설정 
    public boolean updatePassword(String userId, String newPassword) {
        String sql = "UPDATE user SET password = ? WHERE userId = ?";
        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword); // 암호화된 비밀번호
            pstmt.setString(2, userId);
            int result = pstmt.executeUpdate();
            return result > 0; // 비밀번호가 성공적으로 업데이트되었는지 확인
        } catch (SQLException e) {
            System.err.println("비밀번호 재설정 실패 : " + e);
            return false;
        }
    }
    
    // ID 바탕으로 이름 출력 (통장 목록에서 이름)
    public String getUsernameById(String userId) {
        String sql = "SELECT username FROM user WHERE userId = ?";
        String username = null;

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    username = rs.getString("username");
                }
            }
        } catch (SQLException e) {
            System.err.println("이름 조회 실패 : " + e);
        }

        return username;
    }
    
    // 사용자 정보 조회 메서드
    public UserBean getUserById(String userId) {
        String sql = "SELECT userId, username, email, birthDate, gender, role, createdAt FROM user WHERE userId =?";
        UserBean user = null;

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    user = new UserBean();
                    user.setUserId(rs.getString("userId"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setBirthDate(rs.getDate("birthDate").toLocalDate());
                    user.setGender(UserBean.Gender.valueOf(rs.getString("gender")));
                    user.setRole(UserBean.Role.valueOf(rs.getString("role")));
                    user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                }
            }
        } catch (SQLException e) {
            System.err.println("사용자 정보 조회 실패 : " + e);
        }

        return user;
    }
    
    // Admin Page
    // 유저 리스트 가져오기 (페이징 및 검색 기능 포함)
    public List<UserBean> getUserList(int startRow, int limit, String searchKeyword, String joinDate) {
        List<UserBean> userList = new ArrayList<>();
        String sql = "SELECT userId, username, email, birthDate, gender, role, createdAt FROM user WHERE role != 'ROOT'";

        // Convert searchKeyword for gender if applicable
        if ("남성".equals(searchKeyword)) {
            searchKeyword = "MALE";
        } else if ("여성".equals(searchKeyword)) {
            searchKeyword = "FEMALE";
        } else if ("선택안함".equals(searchKeyword)) {
            searchKeyword = "ANONY";
        }

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql += " AND (userId LIKE ? OR username LIKE ? OR email LIKE ? OR gender LIKE ?)";
        }

        if (joinDate != null && !joinDate.isEmpty()) {
            sql += " AND DATE(createdAt) = ?";
        }

        sql += " ORDER BY createdAt DESC LIMIT ?, ?";

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            int paramIndex = 1;

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
            }

            if (joinDate != null && !joinDate.isEmpty()) {
                pstmt.setString(paramIndex++, joinDate);
            }

            pstmt.setInt(paramIndex++, startRow);
            pstmt.setInt(paramIndex, limit);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    UserBean user = new UserBean();
                    user.setUserId(rs.getString("userId"));
                    user.setUsername(rs.getString("username"));
                    user.setEmail(rs.getString("email"));
                    user.setBirthDate(rs.getDate("birthDate").toLocalDate());
                    user.setGender(UserBean.Gender.valueOf(rs.getString("gender")));
                    user.setRole(UserBean.Role.valueOf(rs.getString("role")));
                    user.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                    userList.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("유저 리스트 가져오기 실패: " + e);
        }
        return userList;
    }

    // 총 유저수 가져오기
    public int getTotalUsers(String searchKeyword, String joinDate) {
        String sql = "SELECT COUNT(*) FROM user WHERE role != 'ROOT'";

        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            sql += " AND (userId LIKE ? OR username LIKE ? OR email LIKE ? OR gender LIKE ?)";
        }

        if (joinDate != null && !joinDate.isEmpty()) {
            sql += " AND DATE(createdAt) = ?";
        }

        try (Connection con = ds.getConnection(); PreparedStatement pstmt = con.prepareStatement(sql)) {
            int paramIndex = 1;

            if (searchKeyword != null && !searchKeyword.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
                pstmt.setString(paramIndex++, "%" + searchKeyword + "%");
            }

            if (joinDate != null && !joinDate.isEmpty()) {
                pstmt.setString(paramIndex, joinDate);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("총 유저 수 가져오기 실패: " + e);
        }
        return 0;
    }
    
 // 관리자 권한 변경
    public boolean changeUserRole(String userId, String newRole) {
        String sql = "UPDATE user SET role = ? WHERE userId = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, newRole);
            pstmt.setString(2, userId);
            int result = pstmt.executeUpdate();
            System.out.println("Update result: " + result);
            return result > 0; // 성공 여부 확인
        } catch (Exception e) {
            System.err.println("관리자 변경 실패 : " + e);
            return false;
        }
    }

    // 관리자 여부 확인 메소드
    public boolean isAdmin(String userId) {
        String sql = "SELECT role FROM user WHERE userId = ?";

        try (Connection con = ds.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "ADMIN".equals(rs.getString("role"));
                }
            }
        } catch (Exception e) {
            System.err.println("관리자 여부 확인 실패 : " + e);
        }
        return false;
    }
}
