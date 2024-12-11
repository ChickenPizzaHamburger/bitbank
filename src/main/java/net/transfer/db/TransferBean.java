package net.transfer.db;

import java.time.LocalDateTime;

/*
CREATE TABLE `transfer` (
	`sender_account` VARCHAR(15) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`receiver_account` VARCHAR(15) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`send_time` TIMESTAMP NOT NULL,
	`send_amount` BIGINT NOT NULL,
	`tag` VARCHAR(50) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	INDEX `FK__account` (`sender_account`) USING BTREE,
	INDEX `FK__account_2` (`receiver_account`) USING BTREE,
	CONSTRAINT `FK__account` FOREIGN KEY (`sender_account`) REFERENCES `account` (`account_num`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FK__account_2` FOREIGN KEY (`receiver_account`) REFERENCES `account` (`account_num`) ON UPDATE NO ACTION ON DELETE NO ACTION
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
 */

public class TransferBean {
    private String sender_account;
    private String receiver_account;
    private LocalDateTime send_time;  // LocalDate -> LocalDateTime으로 변경
    private long send_amount;
    private String tag;
    
    // 기본 생성자
    public TransferBean() {}
    
    public TransferBean(String sender_account, String receiver_account, LocalDateTime send_time, long send_amount, String tag) {
        this.sender_account = sender_account;
        this.receiver_account = receiver_account;
        this.send_time = send_time;
        this.send_amount = send_amount;
        this.tag = tag;
    }

    // Getter 및 Setter
    public String getSender_account() {
        return sender_account;
    }

    public void setSender_account(String sender_account) {
        this.sender_account = sender_account;
    }

    public String getReceiver_account() {
        return receiver_account;
    }

    public void setReceiver_account(String receiver_account) {
        this.receiver_account = receiver_account;
    }

    public LocalDateTime getSend_time() {
        return send_time;
    }

    public void setSend_time(LocalDateTime send_time) {
        this.send_time = send_time;
    }

    public long getSend_amount() {
        return send_amount;
    }

    public void setSend_amount(long send_amount) {
        this.send_amount = send_amount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TransferBean [sender_account=" + sender_account + 
                ", receiver_account=" + receiver_account + 
                ", send_time=" + send_time + ", send_amount=" + send_amount + 
                ", tag=" + tag + "]";
    }
}
