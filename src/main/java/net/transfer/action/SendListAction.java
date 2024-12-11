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

        // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        if (userId == null) {
            response.sendRedirect("./loginView.use"); // 로그인 페이지로 리다이렉트
            return null;
        }

        // URL 파라미터로 받은 계좌 번호
        String accountNum = request.getParameter("num");
        System.out.println("Num : " + accountNum);

        // Num을 가지고, account_type, account_amount를 가져와서 account_num과 account_type, account_amount를 전달

        // TransferDAO 객체 생성하여 계좌 목록 조회
        TransferDAO dao = new TransferDAO();
        List<Map<String, Object>> accountList = dao.getAccountDetailsByNum(accountNum);

        // 계좌가 없으면 에러 메시지를 세션에 저장
        if (accountList.isEmpty()) {
            request.getSession().setAttribute("errorMessage", "등록된 계좌가 없습니다. 계좌를 개설해주세요.");
        } else {
        	request.getSession().setAttribute("accountNum", accountNum);
            // 세션에 account 리스트 저장
            request.getSession().setAttribute("accountList", accountList);
        }

        // ActionForward 객체 생성 후 경로 설정
        ActionForward forward = new ActionForward();
        forward.setPath("./sendAccountView.tr"); // 계좌 송금 페이지로 리다이렉트
        forward.setRedirect(true); // 리다이렉트 방식
        return forward;
    }
}
