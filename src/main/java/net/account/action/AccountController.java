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

        System.out.println("command : " + command);

        switch (command) {
            case "accountView.ac": // 계좌 목록 화면
                action = new AccountAction();
                break;

            case "accountInfoView.ac": // 계좌 정보 화면
                forward = new ActionForward();
                forward.setRedirect(false);
                forward.setPath("/account/AccountInfoView.jsp");
                break;

            case "accountAddView.ac": // 통장 개설 화면
                forward = new ActionForward();
                forward.setRedirect(false);
                forward.setPath("/account/AccountAddView.jsp");
                break;

            case "accountAction.ac": // 통장 메인(목록) 화면
                action = new AccountAction();
                break;

            case "accountAddAction.ac": // 계좌 개설 (AccountView → # → AccountAddView)
                action = new AccountAddAction();
                break;

            case "accountInfoAction.ac": // 계좌 정보 화면 (수정)
                // accountNum 파라미터를 받기
                String accountNum = request.getParameter("accountNum");
                if (accountNum != null && !accountNum.isEmpty()) {
                    action = new AccountInfoAction(); // AccountInfoAction 실행
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "계좌 번호가 필요합니다.");
                    return;
                }
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
