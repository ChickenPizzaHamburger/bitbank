package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.util.Action;
import net.util.ActionForward;

public class AdminAccountPwdChangeCodeAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 세션에서 인증번호 가져오기
        HttpSession session = request.getSession();
        String storedVerificationCode = (String) session.getAttribute("verificationCode");
        String accountNum = request.getParameter("accountNum");
        String enteredCode = request.getParameter("code");

        // 인증번호 일치 여부 확인
        if (storedVerificationCode != null && storedVerificationCode.equals(enteredCode)) {
            // 인증번호가 일치하면 계좌 암호 변경 로직 처리
            // 예: 암호 변경 DB 작업 수행

            // 계좌 암호 변경 완료 후 세션에서 인증번호 삭제
            session.removeAttribute("verificationCode");

            // 성공 응답
            response.getWriter().write("{\"success\": true}");
        } else {
            // 인증번호 불일치 시 실패 응답
            response.getWriter().write("{\"success\": false, \"error\": \"인증번호가 올바르지 않습니다.\"}");
        }

        return null;
    }
}