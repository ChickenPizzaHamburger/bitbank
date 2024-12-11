package net.account.action;

import net.account.db.AccountBean;
import net.account.db.AccountDAO;

public class AccountService {

    private AccountDAO accountDAO = new AccountDAO();  // 계좌 정보 조회 DAO 객체

    // 계좌 정보를 조회하는 메소드
    public AccountBean getAccountInfo(String accountNum) {
        return accountDAO.getAccountInfo(accountNum);  // DAO에서 계좌 정보 반환
    }
}
