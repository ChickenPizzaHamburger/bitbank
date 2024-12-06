package net.transfer.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.transfer.db.TransferDAO;
import net.util.Action;
import net.util.ActionForward;

public class SendListAction implements Action { // 계좌 목록 조회
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 현재 로그인된 사용자 ID
        String userId = (String) request.getSession().getAttribute("userId");
        
        if (userId == null) {
            response.sendRedirect("./loginView.use"); // 로그인 페이지로 리다이렉트
            return null;
        }
        
        TransferDAO dao = new TransferDAO();
        List<Map<String, Object>> accountList = dao.getAccountsById(userId);
        
        if (accountList == null || accountList.isEmpty()) {
            request.setAttribute("errorMessage", "등록된 계좌가 없습니다. 계좌를 개설해주세요.");
        } else {
            request.setAttribute("accountList", accountList);
        }

        ActionForward forward = new ActionForward();
        forward.setPath("/sendAccountView.tr");
        forward.setRedirect(false);
        return forward;
    }
}