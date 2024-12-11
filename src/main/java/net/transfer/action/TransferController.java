package net.transfer.action;

import javax.servlet.*;
import javax.servlet.http.*;

import net.util.Action;
import net.util.ActionForward;

import java.io.*;

public class TransferController extends HttpServlet {

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String command = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        ActionForward forward = null;
        Action action = null;

        switch (command) {
        	// View로 바로 넘어가는 형태
	        case "sendAccountView.tr":  // 송금 계좌 화면
	            forward = new ActionForward();
	            forward.setRedirect(false); // 포워드로 처리
	            forward.setPath("/transfer/SendAccountView.jsp"); // sendAccountView.jsp로 포워드
	            break;
	            
	        case "sendAmountView.tr": 	// 송금 금액 화면
	        	forward = new ActionForward();
	            forward.setRedirect(false); // 포워드로 처리
	            forward.setPath("/transfer/SendAmountView.jsp"); // sendAmountView.jsp로 포워드
	            break;
	            
	        case "completeTransferView.tr": // 결과 화면
	        	forward = new ActionForward();
	        	forward.setRedirect(false);
	        	forward.setPath("/transfer/CompleteTransferView.jsp");
	        	break;
        
	        // Action을 통해서 넘어가는 형태
            case "sendListAction.tr": // 계좌 목록 조회 (MainView → # → SendView)
                action = new SendListAction();
                break;

            case "validateTransferAction.tr": // 송금 준비 (SendView → # → PasswardView)
                action = new ValidateTransferAction(); 
                break;

            case "transferAction.tr": // 송금 실행 (PasswardView → # → ResultView)
                action = new TransferAction(); 
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "요청에 해당하는 액션이 없습니다.");
                return;
        }

        // Action이 존재하면 execute() 메서드를 실행하여 ActionForward를 반환
        if (action != null) {
            try {
                forward = action.execute(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (forward != null) {
            if (forward.isRedirect()) {
                response.sendRedirect(forward.getPath());
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
                dispatcher.forward(request, response);
            }
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }
}