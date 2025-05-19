package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.AccessLog;
import uts.isd.model.DAO.DAO;
import uts.isd.model.Person.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


@WebServlet("/ViewProfile")
public class ViewProfileServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedInUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        User user = (User) session.getAttribute("loggedInUser");

        List<AccessLog> logs;
        try {
            DAO dao = new DAO();
            logs = dao.AccessLogs().findByEmail(user.getEmail());
        } catch (SQLException e) {
            throw new ServletException("Unable to load access logs", e);
        }

        req.setAttribute("logs", logs);
        req.getRequestDispatcher("/ViewProfile.jsp").forward(req, resp);
    }
}
