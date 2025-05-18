package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.Person.User;
import uts.isd.model.DAO.DAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 1) Get existing session (don't create a new one)
        HttpSession session = req.getSession(false);
        if (session != null) {
            // 2) Pull the user from session
            User user = (User) session.getAttribute("loggedInUser");
            if (user != null) {
                // 3) Record the LOGOUT event
                try {
                    DAO dao = new DAO();
                    dao.AccessLogs().addLog(user.getEmail(), "LOGOUT");
                    dao.close();
                } catch (SQLException e) {
                    throw new ServletException("Failed to record logout", e);
                }
            }
            // 4) Invalidate the session
            session.invalidate();
        }
        // 5) Redirect home
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    // Optional: handle GET requests the same way
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
