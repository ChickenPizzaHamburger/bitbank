package net.user.db;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/*
CREATE TABLE `user` (
	`userId` VARCHAR(20) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`username` VARCHAR(10) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`password` VARCHAR(60) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`email` VARCHAR(30) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`birthDate` DATE NULL DEFAULT NULL,
	`gender` ENUM('MALE','FEMALE','ANONY') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`role` ENUM('ADMIN','USER','ROOT') NOT NULL DEFAULT 'USER' COLLATE 'utf8mb4_0900_ai_ci',
	`createdAt` TIMESTAMP NULL DEFAULT (now()),
	PRIMARY KEY (`userId`) USING BTREE,
	UNIQUE INDEX `email` (`email`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
 */

public class UserBean {
    private String userId;              // 유저 ID (소셜 ID의 경우 '$' 포함)
    private String username;            // 사용자 이름
    private String password;            // 비밀번호 (소셜 회원일 경우 NULL 허용)
    private String email;               // 이메일
    private LocalDate birthDate;        // 생년월일
    private Gender gender;              // 성별 (Enum)
    private Role role = Role.USER;      // 역할 (Enum)
    private LocalDateTime createdAt;    // 가입 일자

    // 성별 Enum
    public enum Gender {
        MALE, FEMALE, ANONY
    }

    // 역할 Enum
    public enum Role {
        ROOT, ADMIN, USER
    }

    // 기본 생성자
    public UserBean() {}

    // 모든 필드를 초기화하는 생성자
    public UserBean(String userId, String username, String password, String email, LocalDate birthDate, Gender gender, Role role, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.role = role;
        this.createdAt = createdAt;
    }

    // Getter 및 Setter
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getFormattedCreatedAt() {
        // 데이터베이스에서 가져온 createdAt을 그대로 사용
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());
        LocalDateTime formattedTime = createdAt.minusHours(9);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E요일 a hh:mm:ss");
        String formattedTimeStr = formattedTime.format(formatter);
        
        System.out.println("Formatted Created At: " + formattedTimeStr);
        return formattedTimeStr;
    }

    // SQL 타입으로 변환하는 메서드들
    public Date getSqlBirthDate() {
        return birthDate == null ? null : Date.valueOf(birthDate);
    }

    public Timestamp getSqlCreatedAt() {
        return createdAt == null ? null : Timestamp.valueOf(createdAt);
    }

    @Override
    public String toString() {
        return "UserBean [userId=" + userId + ", username=" + username + 
                ", password=" + password + ", email=" + email + 
                ", birthDate=" + birthDate + ", gender=" + gender + 
                ", role=" + role + ", createdAt=" + createdAt + "]";
    }  
}
