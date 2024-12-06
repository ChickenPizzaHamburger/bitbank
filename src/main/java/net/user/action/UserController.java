package net.user.action;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.util.Action;
import net.util.ActionForward;

public class UserController extends HttpServlet {

    protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 현재 요청 URI를 가져옵니다.
        String requestURI = request.getRequestURI();
        // URI에서 마지막 '/' 이후의 부분을 추출합니다 (예: index.use)
        String command = requestURI.substring(requestURI.lastIndexOf("/") + 1);

        // ActionForward 객체 생성
        ActionForward forward = null;
        Action action = null;

        // command에 따라 처리할 작업을 설정
        switch (command) {
        	// View로 바로 넘어가는 형태
        	case "index.use": // index.use로 해도 로그인으로 들어가도록 한다.
            case "loginView.use":  // 로그인 화면 (첫 화면)
                forward = new ActionForward();
                forward.setRedirect(false); // 포워드로 처리
                forward.setPath("/user/LoginView.jsp"); // loginView.jsp로 포워드
                break;

            case "signupView.use": // 회원가입 화면
                forward = new ActionForward();
                forward.setRedirect(false); // 포워드로 처리
                forward.setPath("/user/SignupView.jsp"); // signupView.jsp로 이동
                break;
                
            case "findIdView.use": // 아이디 찾기 화면
                forward = new ActionForward();
                forward.setRedirect(false); // 포워드로 처리
                forward.setPath("/user/FindIdView.jsp"); // FindIdView.jsp로 이동
                break;
                
            case "findIdResultView.use": // 아이디 찾기 결과 화면
            	forward = new ActionForward();
                forward.setRedirect(false); // 포워드로 처리
                forward.setPath("/user/FindIdResultView.jsp"); // FindIdResultView.jsp로 이동
                break;
                
            case "findPwdView.use": // 비밀번호 찾기 화면
                forward = new ActionForward();
                forward.setRedirect(false); // 포워드로 처리
                forward.setPath("/user/FindPwdView.jsp"); // FindPwdView.jsp로 이동
                break;
                
            case "findPwdResultView.use": // 아이디 찾기 결과 화면
            	forward = new ActionForward();
                forward.setRedirect(false); // 포워드로 처리
                forward.setPath("/user/FindPwdResultView.jsp"); // FindPwdResultView.jsp로 이동
                break;
            	
            // Action을 통해서 넘어가는 형태
            case "signupAction.use": // 회원가입 처리 (SignupView → # → LoginView)
            	action = new SignupAction();
            	break;
            	
            case "socialSignupAction.use": // 소셜 회원가입 처리 (LoginView → # → LoginAction)
            	action = new SocialSignupAction();
            	break;

            case "loginAction.use": // 로그인 처리 (LoginView/SocialSignupAction → # → MainView)
                action = new LoginAction();
                break;
                
            case "findIdAction.use": // 아이디 찾기 처리 (FindIdView → # → FindIdResultView)
            	action = new FindIdAction();
            	break;
            
            case "findPwdAction.use": // 비밀번호 찾기 처리 (FindPwdView → # → FindPwdResultView)
            	action = new FindPwdAction();
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

        // ActionForward 처리
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
