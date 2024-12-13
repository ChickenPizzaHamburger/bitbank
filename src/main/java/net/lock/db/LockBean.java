package net.lock.db;

import java.sql.Timestamp;

/*
CREATE TABLE `lock` (
	`lock_id` BIGINT NOT NULL AUTO_INCREMENT,
	`account_num` VARCHAR(20) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`locked_amount` BIGINT NOT NULL,
	`lock_start_date` TIMESTAMP NOT NULL,
	`lock_end_date` TIMESTAMP NOT NULL,
	`is_active` TINYINT(1) NOT NULL DEFAULT '1',
	PRIMARY KEY (`lock_id`) USING BTREE,
	INDEX `account_num` (`account_num`) USING BTREE,
	CONSTRAINT `lock_ibfk_1` FOREIGN KEY (`account_num`) REFERENCES `account` (`account_num`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
 */

public class LockBean {
    private long lockId;
    private String accountNum;
    private long lockedAmount;
    private String lockType;
    private Timestamp lockStartDate;
    private Timestamp lockEndDate;
    private boolean isActive;

    // Getters and Setters
    public long getLockId() {
        return lockId;
    }

    public void setLockId(long lockId) {
        this.lockId = lockId;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public long getLockedAmount() {
        return lockedAmount;
    }

    public void setLockedAmount(long lockedAmount) {
        this.lockedAmount = lockedAmount;
    }

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public Timestamp getLockStartDate() {
        return lockStartDate;
    }

    public void setLockStartDate(Timestamp lockStartDate) {
        this.lockStartDate = lockStartDate;
    }

    public Timestamp getLockEndDate() {
        return lockEndDate;
    }

    public void setLockEndDate(Timestamp lockEndDate) {
        this.lockEndDate = lockEndDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
