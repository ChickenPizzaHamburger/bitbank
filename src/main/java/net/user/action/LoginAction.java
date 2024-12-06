package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class LoginAction implements Action { // 로그인 시도
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8"); // 인코딩

        // 로그인 폼에서 넘어온 ID
        String userId = (String) request.getAttribute("userId");
        System.out.println("로그인 시도된 userId: " + userId);  // 디버깅용
        
        
        // 로그인 타입 자동 판단: userId가 "$"로 시작하면 소셜 로그인으로 판단
        boolean isSocialLogin = userId != null && userId.startsWith("$");
        System.out.println("소셜 로그인 여부: " + isSocialLogin);  // 디버깅용

        UserDAO userDAO = new UserDAO();
        boolean isValidUser = false;

        // 로그인 타입에 따라 처리 분기
        if (isSocialLogin) {
            // 소셜 로그인: ID만 확인 (예: 카카오 로그인)
        	System.out.println("소셜 로그인 처리 중...");
            isValidUser = userDAO.checkUserIdExists(userId);
            System.out.println("소셜 로그인 사용자 존재 여부: " + isValidUser);  // 디버깅용
        } else {
        	userId = request.getParameter("userId");
        	String password = request.getParameter("userPwd");
        	
            // 기본 로그인: 사용자 ID와 비밀번호 확인
            isValidUser = userDAO.checkUserIdExists(userId); // 먼저 사용자 존재 여부 확인
            if (isValidUser) {
                // 비밀번호까지 확인 (일반 로그인에서는 비밀번호 검증 필요)
                isValidUser = userDAO.checkPassword(userId, password);
            }
        }

        ActionForward forward = new ActionForward();
        if (isValidUser) {
            // 로그인 성공시 세션에 사용자 정보 저장
            request.getSession().setAttribute("userId", userId);  // 세션에 userId 저장

            // 디버깅용
            System.out.println("로그인 성공! 세션에 저장된 userId: " + userId);
            
            // 로그인 후 리다이렉션
            forward.setPath("./mainView.ac"); // 예시: 로그인 후 홈 페이지로 이동
            forward.setRedirect(true); // 리다이렉션
        } else {
            // 로그인 실패시 에러 메시지 전달
            request.setAttribute("errorMessage", "아이디나 비밀번호가 잘못되었습니다.");
            forward.setPath("/loginView.use"); // 로그인 페이지로 돌아감
            forward.setRedirect(false); // 포워드로 이동
        }
        
        return forward;
    }
}