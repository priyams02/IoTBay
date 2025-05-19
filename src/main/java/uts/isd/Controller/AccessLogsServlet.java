package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.AccessLog;
import uts.isd.model.Person.User;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.AccessLogDBManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/AccessLogs")
public class AccessLogsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) ensure user is logged in
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("loggedInUser");
        String email = user.getEmail();

        // 2) load logs
        List<AccessLog> logs;
        try  {
            DAO dao = new DAO();
            AccessLogDBManager logMgr = dao.AccessLogs();

            String dateParam = req.getParameter("date"); // yyyy-MM-dd
            if (dateParam != null && !dateParam.isEmpty()) {
                LocalDate date = LocalDate.parse(dateParam);
                logs = logMgr.findByEmailAndDate(email, date);
            } else {
                logs = logMgr.findByEmail(email);
            }
        } catch (SQLException e) {
            throw new ServletException("Unable to load access logs", e);
        }

        // 3) forward to JSP
        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/ViewProfile.jsp")
                .forward(req, resp);
    }
}