package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Don't create a new session if one doesn't exist
        HttpSession session = req.getSession(false);
        if (session != null) {
            // Invalidate the entire session (clears everything, including "User")
            session.invalidate();
        }
        // Redirect back to home
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }

    // Optional: allow GET to also log out
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
