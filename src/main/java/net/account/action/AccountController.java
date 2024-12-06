package net.account.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.util.Action;
import net.util.ActionForward;

public class AccountController extends HttpServlet {

	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
        String command = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        ActionForward forward = null;
        Action action = null;

        switch (command) {
        	// View로 바로 넘어가는 형태
        	case "mainView.ac": // 메인 화면
	            forward = new ActionForward();
	            forward.setRedirect(false); // 포워드로 처리
	            forward.setPath("/account/MainView.jsp"); // MainView.jsp로 포워드
	            break;
        
        	case "addView.ac": // 통장 개설 화면
        		forward = new ActionForward();
        		forward.setRedirect(false);
        		forward.setPath("/account/AddView.jsp"); // AddView.jsp로 리다이렉트
        		break;
        		
        	case "accountView.ac": // 계좌 목록 화면
        		forward = new ActionForward();
        		forward.setRedirect(false);
        		forward.setPath("/account/AccountView.jsp"); // AccountView.jsp로 리다이렉트
        		break;
        		
        	case "accountInfoView.ac": // 계좌 정보 화면
        		forward = new ActionForward();
        		forward.setRedirect(false);
        		forward.setPath("/account/AccountInfoView.jsp"); // AccountInfoView.jsp로 리다이렉트
        		break;
        		
	        // Action을 통해서 넘어가는 형태
	        case "addAction.ac": // 계좌 개설 (MainView → # → MainView)
	        	action = new AddAction();
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
