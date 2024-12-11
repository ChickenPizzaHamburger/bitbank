package net.account.db;

import java.time.LocalDate;

/*
CREATE TABLE `account` (
	`user_id` VARCHAR(20) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`account_num` VARCHAR(15) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`account_password` VARCHAR(60) NOT NULL DEFAULT '' COLLATE 'utf8mb4_0900_ai_ci',
	`account_type` ENUM('COMMON','PLUSBOX','INSTALLMENT') NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`account_amount` BIGINT NOT NULL,
	`account_date` DATE NULL DEFAULT NULL,
	PRIMARY KEY (`account_num`) USING BTREE,
	INDEX `FK_account_user` (`user_id`) USING BTREE,
	CONSTRAINT `FK_account_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`userId`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
 */

public class AccountBean {
	private String user_id;
	private String account_num;
	private String account_password;
	private Type account_type;
	private long account_amount;
	private LocalDate account_date;
	
	// 타입 Enum
	public enum Type {
		COMMON, PLUSBOX, INSTALLMENT
	}
	
	// 기본 생성자
	public AccountBean() {}
	
	// 모든 필드를 초기화하는 생성자
	public AccountBean(String account_num, String account_password, Type account_type, long account_amount, LocalDate account_date) {
		this.account_num = account_num;
		this.account_password = account_password;
		this.account_type = account_type;
		this.account_amount = account_amount;
		this.account_date = account_date;
	}

	// Getter 및 Setter
	public String getUser_id() { 
        return user_id;
    }

    public void setUser_id(String user_id) { 
        this.user_id = user_id;
    }
	
	public String getAccount_num() {
		return account_num;
	}

	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}

	public String getAccount_password() {
		return account_password;
	}

	public void setAccount_password(String account_password) {
		this.account_password = account_password;
	}

	public Type getAccount_type() {
		return account_type;
	}

	public void setAccount_type(Type account_type) {
		this.account_type = account_type;
	}

	public long getAccount_amount() {
		return account_amount;
	}

	public void setAccount_amount(long account_amount) {
		this.account_amount = account_amount;
	}

	public LocalDate getAccount_date() {
		return account_date;
	}

	public void setAccount_date(LocalDate account_date) {
		this.account_date = account_date;
	}

	@Override
	public String toString() {
		return "AccountBean [user_id=" + user_id + 
				", account_num=" + account_num + ", account_password=" + account_password + 
				", account_type=" + account_type + ", account_amount=" + account_amount + 
				", account_date=" + account_date + "]";
	}
	
	
}
