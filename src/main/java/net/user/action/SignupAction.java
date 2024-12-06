package net.user.action;

import java.time.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.user.db.UserBean;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class SignupAction implements Action { // 회원가입 시도

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	// 요청 데이터 인코딩 설정
        request.setCharacterEncoding("UTF-8");
    	
        // 폼 데이터 가져오기
        String userId = request.getParameter("userId");
        String username = request.getParameter("userName");
        String password = request.getParameter("userPwd");
        String email = request.getParameter("userEmail");
        String birthDate = request.getParameter("birthDate");
        String gender = request.getParameter("gender");
        String role = "USER";

        // UserBean 객체 생성 및 데이터 설정
        UserBean user = new UserBean();
        user.setUserId(userId);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setBirthDate(birthDate != null ? LocalDate.parse(birthDate) : null);
        user.setGender(UserBean.Gender.valueOf(gender.toUpperCase()));
        user.setRole(UserBean.Role.valueOf(role.toUpperCase()));

        // UserDAO를 이용한 회원가입 처리
        UserDAO userDAO = new UserDAO();
        boolean isRegistered = userDAO.signup(user);

        // 회원가입 성공 여부에 따른 처리
        ActionForward forward = new ActionForward();
        if (isRegistered) {
            forward.setPath("loginView.use");
            forward.setRedirect(true);
        } else {
            request.setAttribute("error", "회원가입에 실패했습니다. 다시 시도해주세요.");
            forward.setPath("signupView.use"); // 다시 폼 페이지로 이동
            forward.setRedirect(false);
        }
        return forward;
    }
}