package net.user.action;

import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.user.db.UserBean;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;
import org.mindrot.jbcrypt.BCrypt; // BCrypt 암호화 사용

public class SignupAction implements Action {

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

        // 각 필드에 대한 에러 메시지를 초기화
        String errorId = null;
        String errorPwd = null;
        String errorEmail = null;
        String errorName = null;
        
        // 유효성 검사
        String errorMsg = null;
        if (!isValidId(userId)) {
        	errorId = "아이디는 4~12자의 영어 또는 영어+숫자 조합으로만 입력 가능하며, 숫자로 시작할 수 없습니다!";
        } else if (!isValidPassword(password, userId, birthDate)) {
        	errorPwd = "비밀번호는 8자 이상의 영어, 숫자, 특수문자를 조합해야 하며, 아이디나 생년월일과 동일할 수 없습니다!";
        } else if (!isValidEmail(email)) {
        	errorEmail = "이메일 형식을 올바르게 입력하세요! 예: example@domain.com";
        } else if (!isValidName(username)) {
        	errorName = "이름은 한글로 2자 이상 입력해야 합니다!";
        }
        
        // 아이디나 이메일 중복 확인
        UserDAO userDAO = new UserDAO();
        userDAO.testConnection();
        if (userDAO.isUserIdDuplicate(userId)) {
        	errorId = "이미 존재하는 아이디입니다.";
            System.out.println("Duplicate ID found: " + userId);
        } else if (userDAO.isEmailDuplicate(email)) {
        	errorEmail = "이미 존재하는 이메일입니다.";
            System.out.println("Duplicate Email found: " + email);
        }

        // 에러 메시지가 있으면 request에 저장하고 폼으로 다시 이동
        if (errorId != null || errorPwd != null || errorEmail != null || errorName != null) {
            request.setAttribute("errorId", errorId);
            request.setAttribute("errorPwd", errorPwd);
            request.setAttribute("errorEmail", errorEmail);
            request.setAttribute("errorName", errorName);

            ActionForward forward = new ActionForward();
            forward.setPath("signupView.use"); // 다시 폼 페이지로 이동
            forward.setRedirect(false);
            return forward;
        }
        

        // UserBean 객체 생성 및 데이터 설정
        UserBean user = new UserBean();
        user.setUserId(userId);
        user.setUsername(username);
        user.setEmail(email);
        user.setBirthDate(birthDate != null ? LocalDate.parse(birthDate) : null);
        user.setGender(UserBean.Gender.valueOf(gender.toUpperCase()));
        user.setRole(UserBean.Role.valueOf(role.toUpperCase()));

        
        // 비밀번호 암호화
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashedPassword); // 암호화된 비밀번호 저장
        

        // UserDAO를 이용한 회원가입 처리
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

    // 유효성 검사 메소드들
    private boolean isValidId(String userId) {
        return userId != null && userId.matches("^[a-zA-Z][a-zA-Z0-9]{3,11}$");
    }

    private boolean isValidPassword(String password, String userId, String birthDate) {
    	String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
        return password != null && password.matches(passwordRegex) && !password.equals(userId) && !password.equals(birthDate);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    private boolean isValidName(String username) {
        return username != null && username.matches("^[가-힣]{2,}$");
    }
}