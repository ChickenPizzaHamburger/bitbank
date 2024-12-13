package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class AdminChangeUserRoleAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 현재 로그인한 사용자 정보 가져오기 (세션에서 확인)
        UserDAO userDAO = new UserDAO();

        String currentUserId = (String) request.getSession().getAttribute("userId");
        System.out.println("Current User ID: " + currentUserId);

        // 관리자 권한 확인 로직
        if (!userDAO.isAdmin(currentUserId)) {
            // 관리자 권한이 없으면 에러 처리
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("success", false);
            jsonResponse.put("message", "권한이 없습니다.");
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse.toString());
            return null;
        }

        String userId = request.getParameter("userId");
        String newRole = request.getParameter("newRole");
        System.out.println("User ID: " + userId);
        System.out.println("New Role: " + newRole);

        boolean success = userDAO.changeUserRole(userId, newRole);

        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("success", success);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse.toString());

        return null;
    }
}