package net.transfer.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.transfer.db.TransferDAO;
import net.util.Action;
import net.util.ActionForward;

public class TransferAction implements Action { // 송금 실행
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	request.setCharacterEncoding("UTF-8"); // 인코딩
    	
        String senderAccount = request.getParameter("senderAccount");
        String receiverAccount = request.getParameter("receiverAccount");
        long amount = Long.parseLong(request.getParameter("amount"));
        String tag = request.getParameter("tag");
        String password = request.getParameter("password"); // 사용자 입력 비밀번호

        long fee = 500; // 송금 수수료
        String rootAccount = "000-00-000000"; // root 계좌
        
        long maxTransferLimit = 10000000; // 송금 한도 설정
        
        // 송금 금액이 한도를 초과하는지 확인
        if (amount > maxTransferLimit) {
            request.setAttribute("errorMessage", "송금 금액이 1회 송금 한도를 초과합니다.");
            request.setAttribute("transferSuccess", false);
            ActionForward forward = new ActionForward();
            forward.setPath("/resultView.tr"); // 결과 화면으로 이동
            forward.setRedirect(false);
            return forward;
        }

        TransferDAO dao = new TransferDAO();

        // 비밀번호 검증
        if (!dao.isPassword(senderAccount, password)) {
            request.setAttribute("errorMessage", "계좌 비밀번호가 올바르지 않습니다.");
            ActionForward forward = new ActionForward();
            forward.setPath("/confirmPasswordView.tr"); // 비밀번호 확인 화면으로 돌아감
            forward.setRedirect(false);
            return forward;
        }
        
        // 송금 수수료를 면제할지 결정 (보내는 계좌와 받는 계좌의 사용자가 같으면 수수료 면제)
        if (dao.isSameUser(senderAccount, receiverAccount)) {
            fee = 0; // 동일 사용자일 경우 송금 수수료 면제
        }

        // A → root 송금
        boolean toRootSuccess = dao.transferMoney(senderAccount, rootAccount, amount + fee, "송금 수수료 포함");

        // root → B 송금
        boolean toReceiverSuccess = false;
        if (toRootSuccess) {
            toReceiverSuccess = dao.transferMoney(rootAccount, receiverAccount, amount, tag);
        }

        if (toRootSuccess && toReceiverSuccess) {
            request.setAttribute("transferSuccess", true);
        } else {
            request.setAttribute("errorMessage", "송금 처리 중 오류가 발생했습니다. 다시 시도해주세요.");
            request.setAttribute("transferSuccess", false);
        }

        ActionForward forward = new ActionForward();
        forward.setPath("/resultView.tr"); // 결과 화면
        forward.setRedirect(false);
        return forward;
    }
}
