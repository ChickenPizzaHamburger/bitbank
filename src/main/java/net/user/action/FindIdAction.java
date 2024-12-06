package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class FindIdAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userEmail = request.getParameter("userEmail");
        String birthDate = request.getParameter("birthDate");
        
        System.out.println("userEmail : " + userEmail);
        System.out.println("birthDate : " + birthDate);
		
        UserDAO userDAO = new UserDAO();
        String id = userDAO.findId(userEmail, birthDate);
        
        request.setAttribute("userId", id);
        
		// 아이디 찾기 성공 여부에 따른 처리
		ActionForward forward = new ActionForward();
        if (id != null) {
            forward.setPath("findIdResultView.use");
            forward.setRedirect(true);
        } else {
            request.setAttribute("error", "아이디 찾기에 실패했습니다. 다시 시도해주세요.");
            forward.setPath("findIdView.use"); // 다시 폼 페이지로 이동
            forward.setRedirect(false);
        }
		return forward;
	}

}
