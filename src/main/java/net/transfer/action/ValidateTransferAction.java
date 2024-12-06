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
        String amountParam = request.getParameter("amount");
        String tag = request.getParameter("tag");
        
        long fee = 500; // 송금 수수료
        
        // 송금 금액이 20자리 이상이면 Long 범위를 초과한다고 판단
        if (amountParam.length() > 19 || (amountParam.length() == 19 && amountParam.compareTo(String.valueOf(Long.MAX_VALUE)) > 0)) {
            request.setAttribute("errorMessage", "송금 금액이 너무 큽니다. 올바른 값을 입력해주세요.");
            ActionForward forward = new ActionForward();
            forward.setPath("/resultView.tr");
            forward.setRedirect(false);
            return forward;
        }

        // amountParam이 유효한 숫자이고, 범위가 Long.MAX_VALUE를 넘지 않으면 long으로 변환
        long amount = Long.parseLong(amountParam);

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
        
        // 송금하는 계좌의 잔액 조회
        long senderBalance = dao.getAccountAmount(senderAccount);

        // 송금 금액이 송금자의 잔액에서 수수료를 제외한 금액을 초과하는지 확인
        if (amount + fee > senderBalance) {
            request.setAttribute("errorMessage", "잔액이 부족하여 송금을 진행할 수 없습니다.");
            ActionForward forward = new ActionForward();
            forward.setPath("/resultView.tr");
            forward.setRedirect(false);
            return forward;
        }
        
        // 송금자와 받는 사람의 사용자가 동일한 경우 송금 수수료 면제
        if (dao.isSameUser(senderAccount, receiverAccount)) {
            fee = 0; // 동일 사용자라면 수수료 면제
        }

        // 검증이 통과된 경우 데이터 임시 저장
        request.setAttribute("senderAccount", senderAccount);
        request.setAttribute("receiverAccount", receiverAccount);
        request.setAttribute("amount", amount);
        request.setAttribute("tag", tag);

        // 다음 단계로 이동
        ActionForward forward = new ActionForward();
        forward.setPath("/confirmPasswordView.tr");
        forward.setRedirect(false);
        return forward;
    }
}