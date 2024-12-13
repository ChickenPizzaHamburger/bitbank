package net.account.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.account.db.AccountBean;
import net.account.db.AccountDAO;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class AccountAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 세션에서 userId 가져오기
        String userId = (String) request.getSession().getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
            ActionForward forward = new ActionForward();
            forward.setPath("/loginView.use");
            forward.setRedirect(true);
            return forward;
        }

        // 사용자 이름 가져오기
        UserDAO userDAO = new UserDAO();
        String username = userDAO.getUsernameById(userId);

        System.out.println("유저 이름 : " + username);

        // 사용자 계좌 정보 가져오기
        AccountDAO accountDAO = new AccountDAO();
        List<AccountBean> accountList = accountDAO.getUserAccounts(userId);

        // 총 잔액 계산
        long totalAmount = accountList.stream()
                                      .mapToLong(AccountBean::getAccount_amount)
                                      .sum();

        // JSP에 전달할 데이터 설정
        request.setAttribute("username", username);  		// 사용자 이름
        request.setAttribute("accountList", accountList);  	// 계좌 리스트
        request.setAttribute("totalAmount", totalAmount);	// 총 잔액

        // Forward 방식으로 accountView.jsp로 이동
        ActionForward forward = new ActionForward();
        forward.setPath("/account/AccountView.jsp");
        forward.setRedirect(false);
        return forward;
    }
}
