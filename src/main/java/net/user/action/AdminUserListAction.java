package net.user.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import net.user.db.UserBean;
import net.user.db.UserDAO;
import net.util.Action;
import net.util.ActionForward;

public class AdminUserListAction implements Action {
    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        UserDAO userDAO = new UserDAO();

        // 페이징 처리
        int page = 1;
        int limit = 100; // 한 페이지에 표시할 유저 수
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        int startRow = (page - 1) * limit;

        // 검색 키워드
        String searchKeyword = request.getParameter("searchKeyword");
        String joinDate = request.getParameter("joinDate");

        // 유저 리스트 가져오기
        List<UserBean> userList = userDAO.getUserList(startRow, limit, searchKeyword, joinDate);
        int totalUsers = userDAO.getTotalUsers(searchKeyword, joinDate);
        int totalPages = (int) Math.ceil((double) totalUsers / limit);

        // 세션에 데이터 저장
        request.getSession().setAttribute("userList", userList);
        request.getSession().setAttribute("currentPage", page);
        request.getSession().setAttribute("totalPages", totalPages);
        request.getSession().setAttribute("searchKeyword", searchKeyword);
        request.getSession().setAttribute("joinDate", joinDate);

        // 뷰 경로 설정
        ActionForward forward = new ActionForward();
        forward.setPath("./userListView.use");
        forward.setRedirect(true);

        return forward;
    }
}