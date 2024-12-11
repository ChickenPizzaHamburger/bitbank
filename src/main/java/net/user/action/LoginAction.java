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

        // 로그인 폼에서 넘어온 ID와 비밀번호
        String userId = request.getParameter("userId");
        String password = request.getParameter("userPwd");

        // 로그인 타입 자동 판단: userId가 "$"로 시작하면 소셜 로그인으로 판단
        boolean isSocialLogin = userId != null && userId.startsWith("$");

        UserDAO userDAO = new UserDAO();
        boolean isValidUser = false;

        // 로그인 타입에 따라 처리 분기
        if (isSocialLogin) {
            // 소셜 로그인 처리
            isValidUser = userDAO.checkUserId(userId); // ID만 확인
        } else {
            // 기본 로그인 처리
            isValidUser = userDAO.checkUserId(userId); // ID 존재 여부 확인
            if (isValidUser) {
                // 비밀번호 검증
                isValidUser = userDAO.checkUserPassword(userId, password);
            }
        }

        ActionForward forward = new ActionForward();

        if (isValidUser) {
            // 로그인 성공 시 세션에 사용자 정보 저장
            request.getSession().setAttribute("userId", userId);

            // 성공 후 리다이렉션 (예: 홈 페이지로 이동)
            forward.setPath("/accountAction.ac");
            forward.setRedirect(true);
        } else {
            // 로그인 실패 시 에러 메시지 설정
            String errorMessage = isSocialLogin
                    ? "소셜 로그인에 실패했습니다. 다시 시도해주세요."
                    : "아이디나 비밀번호가 틀렸습니다.";
            request.setAttribute("errorMessage", errorMessage);

            // 로그인 페이지로 포워딩
            forward.setPath("/loginView.use");
            forward.setRedirect(false); // 포워드로 이동
        }

        return forward;
    }
}
