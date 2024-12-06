package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.util.Action;
import net.util.ActionForward;

public class FindPwdAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 비밀번호 찾기 성공 여부에 따른 처리
        ActionForward forward = new ActionForward();
        if (isRegistered) {
            forward.setPath("findPwdResultView.use");
            forward.setRedirect(true);
        } else {
            request.setAttribute("error", "비밀번호 찾기에 실패했습니다. 다시 시도해주세요.");
            forward.setPath("findPwdView.use"); // 다시 폼 페이지로 이동
            forward.setRedirect(false);
        }
        
		return forward;
	}

}
