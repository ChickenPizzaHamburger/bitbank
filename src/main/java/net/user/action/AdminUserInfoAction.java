package net.user.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.account.db.AccountBean;
import net.account.db.AccountDAO;
import net.user.db.UserBean;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class AdminUserInfoAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 사용자 ID 가져오기
        String userId = request.getParameter("userId");

        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("Invalid user ID.");
        }

        // 사용자 정보 조회
        UserDAO userDAO = new UserDAO();
        UserBean user = userDAO.getUserById(userId);

        if (user == null) {
            throw new Exception("User not found.");
        }

        // 계좌 정보 조회
        AccountDAO accountDAO = new AccountDAO();
        List<AccountBean> accountList = accountDAO.getUserAccounts(userId);

        // 세션에 사용자 정보와 계좌 정보 저장
        HttpSession session = request.getSession();
        session.setAttribute("user", user);          // 사용자 정보 세션에 저장
        session.setAttribute("accountList", accountList);  // 계좌 정보 세션에 저장

        // userId를 URL 파라미터로 전달하여 리디렉션
        ActionForward forward = new ActionForward();
        forward.setPath("./userInfoView.use?userId=" + userId);  // userId를 URL에 포함
        forward.setRedirect(true);  // 리디렉션

        return forward;
    }
}
