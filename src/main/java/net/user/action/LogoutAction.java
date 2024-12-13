package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.util.Action;
import net.util.ActionForward;

public class LogoutAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionForward forward = new ActionForward();
		forward.setPath("./loginView.use");
		forward.setRedirect(true);
		
		HttpSession session = request.getSession();
		session.invalidate();
		
		return forward;
	}

}
