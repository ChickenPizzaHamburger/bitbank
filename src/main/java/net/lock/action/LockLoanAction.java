package net.lock.action;

import net.lock.db.LockBean;
import net.lock.db.LockDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class LockLoanAction {
    private LockDAO lockDAO = new LockDAO();

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LockBean lock = createLockFromRequest(request, "LOAN");

        try {
            lockDAO.createLock(lock);
            response.sendRedirect("lock?action=list&account_num=" + lock.getAccountNum());
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private LockBean createLockFromRequest(HttpServletRequest request, String lockType) {
        LockBean lock = new LockBean();
        lock.setAccountNum(request.getParameter("account_num"));
        lock.setLockedAmount(Long.parseLong(request.getParameter("locked_amount")));
        lock.setLockType(lockType);
        lock.setActive(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        try {
            lock.setLockStartDate(new Timestamp(dateFormat.parse(request.getParameter("lock_start_date")).getTime()));
            lock.setLockEndDate(new Timestamp(dateFormat.parse(request.getParameter("lock_end_date")).getTime()));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }

        return lock;
    }
}
