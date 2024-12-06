package net.account.action;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.account.db.AccountBean;
import net.account.db.AccountDAO;
import net.util.Action;
import net.util.ActionForward;

public class AddAction implements Action { // 계좌 개설

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        AccountDAO accountDAO = new AccountDAO();

        // 폼에서 입력받은 비밀번호와 통장 타입
        String accountPassword = request.getParameter("account_password");
        String accountTypeStr = request.getParameter("account_type");
        String accountOffice = request.getParameter("account_office"); // 영업점 받아오기

        // 통장 번호 자동 생성
        String accountNum = accountDAO.generateAccountNumber(accountOffice, accountTypeStr);

        // 통장 타입 Enum 변환
        AccountBean.Type accountType = AccountBean.Type.valueOf(accountTypeStr.toUpperCase());

        // 계좌 금액 및 날짜 설정
        long accountAmount = 0; // 초기 금액 0원
        LocalDate accountDate = LocalDate.now(); // 현재 날짜

        // AccountBean 객체 생성
        AccountBean account = new AccountBean(accountNum, accountPassword, accountType, accountAmount, accountDate);

        boolean isCreated = accountDAO.createAccount(account, request); // 세션을 통해 user_id를 전달

        // 생성 성공 여부에 따라 리다이렉트 처리
        ActionForward forward = new ActionForward();
        if (isCreated) {
            forward.setPath("mainView.ac");  // 통장 개설 성공 페이지로 이동
            forward.setRedirect(true);
        } else {
            forward.setPath("mainView.ac");  // 실패 페이지로 이동
            forward.setRedirect(true);
        }

        return forward;
    }
}