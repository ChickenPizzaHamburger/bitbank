package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.util.Action;
import net.util.ActionForward;

public class VerifyCodeAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
	    HttpSession session = request.getSession();

	    // 세션에서 데이터 가져오기
	    String inputCode = request.getParameter("code");
	    String storedCode = (String) session.getAttribute("verificationCode");
	    Long expirationTime = (Long) session.getAttribute("expirationTime");

	    String message = "인증번호가 올바르지 않습니다. 다시 시도해주세요.";

	    // 만료 시간 검증
	    if (expirationTime != null && System.currentTimeMillis() > expirationTime) {
	        session.removeAttribute("verificationCode");
	        session.removeAttribute("expirationTime");
	        message = "인증번호가 만료되었습니다. 다시 시도해주세요.";
	    }

	    // 인증번호 검증
	    if (storedCode != null && storedCode.equals(inputCode)) {
	        // 성공 시 결과 페이지로 이동
	        ActionForward forward = new ActionForward();
	        forward.setPath("./findPwdResultView.use");
	        forward.setRedirect(true);
	        response.setContentType("application/json");
	        response.getWriter().write("{\"success\": true, \"message\": \"인증번호가 성공적으로 확인되었습니다.\"}");
	        return null;
	    } else {
	    	// 실패 시 JSON 응답
	        response.setContentType("application/json");
	        response.getWriter().write("{\"success\": false, \"message\": \"" + message + "\"}");
	        return null;
	    }
	}
}
