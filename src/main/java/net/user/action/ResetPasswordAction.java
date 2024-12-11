package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;
import org.mindrot.jbcrypt.BCrypt;

public class ResetPasswordAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 요청 데이터 인코딩 설정
        request.setCharacterEncoding("UTF-8");

        // 세션에서 사용자 ID 가져오기
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId"); // 세션에서 가져온 사용자 ID

        if (userId == null) {
            // 세션이 만료된 경우 로그인 페이지로 이동
            ActionForward forward = new ActionForward();
            forward.setPath("loginView.use");
            forward.setRedirect(true);
            return forward;
        }

        // 폼 데이터 가져오기
        String newPassword = request.getParameter("newPassword"); // 새 비밀번호
        String confirmPassword = request.getParameter("confirmPassword"); // 비밀번호 확인
        
        System.out.println("session userId : " + userId);
        System.out.println("Password : " + newPassword);
        System.out.println("confirmPassword : " + confirmPassword);

        // 에러 메시지 초기화
        String errorPwd = null;

        // 유효성 검사
        if (!isValidPassword(newPassword)) {
            errorPwd = "비밀번호는 8자 이상의 영어, 숫자, 특수문자를 조합해야 합니다!";
        } else if (!newPassword.equals(confirmPassword)) {
            errorPwd = "비밀번호와 비밀번호 확인이 일치하지 않습니다!";
        }

        // 에러가 있다면 request에 에러 메시지를 담고, 폼으로 돌아갑니다
        if (errorPwd != null) {
            request.setAttribute("errorPwd", errorPwd);
            ActionForward forward = new ActionForward();
            forward.setPath("findPwdResultView.use"); // 비밀번호 재설정 페이지로 돌아갑니다
            forward.setRedirect(false);
            return forward;
        }

        // UserDAO를 이용하여 비밀번호 변경 처리
        UserDAO userDAO = new UserDAO();
        userDAO.testConnection(); // DB 연결 테스트

        // 새로운 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        // 비밀번호 변경 처리
        boolean isUpdated = userDAO.updatePassword(userId, hashedPassword);

        ActionForward forward = new ActionForward();
        if (isUpdated) {
            // 성공적으로 비밀번호가 변경된 경우 세션 초기화
            session.invalidate(); // 세션 초기화
            request.setAttribute("success", "비밀번호가 성공적으로 재설정되었습니다.");
            forward.setPath("loginView.use"); // 로그인 페이지로 이동
            forward.setRedirect(true);
        } else {
            request.setAttribute("error", "비밀번호 재설정에 실패했습니다. 다시 시도해주세요.");
            forward.setPath("findPwdResultView.use"); // 재설정 페이지로 돌아감
            forward.setRedirect(false);
        }

        return forward;
    }

    // 비밀번호 유효성 검사
    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return password != null && password.matches(passwordRegex);
    }
}