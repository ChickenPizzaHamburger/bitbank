package net.lock.action;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.lock.db.LockBean;
import net.lock.db.LockDAO;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/lock")
public class LockController extends HttpServlet {

    private LockDAO lockDAO = new LockDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        lockDAO.testConnection(); // 데이터베이스 연결 테스트

        // 락 만료 체크
        checkLockExpiration();

        String action = request.getParameter("action");
        switch (action) {
            case "list":
                listLocks(request, response);
                break;
            case "create":
                showCreateLockForm(request, response);
                break;
            case "settings":
                showLockSettings(request, response);
                break;
            default:
                response.sendRedirect("error.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            handleLockCreation(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }
    }

    private void listLocks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNum = request.getParameter("account_num");
        try {
            request.setAttribute("locks", lockDAO.getLocksByAccount(accountNum));
            request.getRequestDispatcher("/lock/lockList.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private void showCreateLockForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("account_num", request.getParameter("account_num"));
        request.getRequestDispatcher("/lock/createLock.jsp").forward(request, response);
    }

    private void showLockSettings(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountNum = request.getParameter("account_num");
        try {
            request.setAttribute("locks", lockDAO.getLocksByAccount(accountNum));
            request.setAttribute("account_num", accountNum);
            request.getRequestDispatcher("/lock/lockSettings.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    // 락 만료 처리 메서드
    public void checkLockExpiration() {
        try {
            // 락 객체 리스트를 가져옴
            List<LockBean> allLocks = lockDAO.getAllLocks();
            for (LockBean lock : allLocks) {
                // 종료 시간이 현재 시간보다 이전이라면 락 해제
                if (lock.getLockEndDate().before(new Timestamp(System.currentTimeMillis())) && lock.isActive()) {
                    // 락 종료 처리
                    lockDAO.handleLockExpiration(lock);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleLockCreation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            new LockWithdrawalAction().execute(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}
