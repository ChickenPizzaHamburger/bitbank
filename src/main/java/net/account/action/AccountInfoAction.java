package net.account.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.account.db.AccountBean;
import net.transfer.db.TransferDAO;
import net.util.Action;
import net.util.ActionForward;

public class AccountInfoAction implements Action {
    private AccountService accountService = new AccountService();

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String accountNum = request.getParameter("accountNum");

        if (accountNum == null || accountNum.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "계좌 번호가 제공되지 않았습니다.");
            return null;
        }

        // 계좌 정보 조회
        AccountBean account = accountService.getAccountInfo(accountNum);
        if (account == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "계좌 정보를 찾을 수 없습니다.");
            return null;
        }

        // 송금 내역 조회
        TransferDAO dao = new TransferDAO();
        List<Map<String, Object>> transferHistory = dao.getTransferHistory(accountNum);

        // 디버깅: 데이터 출력
        System.out.println("Account Info: " + account);
        System.out.println("Transfer History Size: " + (transferHistory != null ? transferHistory.size() : "null"));

        // 데이터 전달
        request.setAttribute("account", account);
        request.setAttribute("transferHistory", transferHistory != null ? transferHistory : List.of());

        // 포워딩
        ActionForward forward = new ActionForward();
        forward.setPath("/account/AccountInfoView.jsp");
        forward.setRedirect(false);
        return forward;
    }
}