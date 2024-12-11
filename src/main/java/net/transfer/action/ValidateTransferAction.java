package net.transfer.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.transfer.db.TransferDAO;
import net.util.Action;
import net.util.ActionForward;

public class ValidateTransferAction implements Action { // 송금 준비 및 검증
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	request.setCharacterEncoding("UTF-8"); // 인코딩
    	
        // 요청 파라미터 읽기
        String senderAccount = request.getParameter("senderAccount");
        String receiverAccount = request.getParameter("receiverAccount");
        
        System.out.println("sender account : " + senderAccount);
        System.out.println("receiver account : " + receiverAccount);

        // DAO 인스턴스 생성
        TransferDAO dao = new TransferDAO();
        
        // 보내는 계좌와 받는 계좌가 같으면 송금 불가 처리
        if (senderAccount.equals(receiverAccount)) {
            request.setAttribute("errorMessage", "송금하는 계좌와 받는 계좌가 동일합니다. 다른 계좌를 입력해주세요.");
            ActionForward forward = new ActionForward();
            forward.setPath("/resultView.tr");
            forward.setRedirect(false);
            return forward;
        }

        // 예비 검증: 수신 계좌 유효성 확인
        if (!dao.isAccountNum(receiverAccount)) {
            // 유효하지 않은 경우 에러 메시지 설정 및 에러 페이지로 이동
            request.setAttribute("errorMessage", "받는 계좌가 유효하지 않습니다.");
            ActionForward forward = new ActionForward();
            forward.setPath("/resultView.tr");
            forward.setRedirect(false);
            return forward;
        }

        // 검증이 통과된 경우 데이터 임시 저장
        request.getSession().setAttribute("senderAccount", senderAccount);
        request.getSession().setAttribute("receiverAccount", receiverAccount);
        
        System.out.println("Session sender : " + request.getSession().getAttribute("senderAccount"));
        System.out.println("Session receiver : " + request.getSession().getAttribute("receiverAccount"));

        // 다음 단계로 이동
        ActionForward forward = new ActionForward();
        forward.setPath("./sendAmountView.tr");
        forward.setRedirect(true);
        return forward;
    }
}