package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.Staff;
import uts.isd.model.Person.User;
import uts.isd.model.DAO.DAO;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/DeleteServlet")
public class DeleteServlet extends HttpServlet {
    private DAO dao;

    @Override
    public void init() throws ServletException {
        try {
            dao = new DAO();
        } catch (SQLException e) {
            throw new ServletException("Unable to initialize data access", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            resp.sendRedirect("Login.jsp");
            return;
        }

        // Note: we store the logged-in user under "User" in RegisterServlet
        User user = (User) session.getAttribute("User");
        if (user == null) {
            resp.sendRedirect("Login.jsp");
            return;
        }

        try {
            if (user.getType() == User.UserType.CUSTOMER) {
                dao.customers().delete((Customer) user);
            } else {
                dao.staff().delete((Staff) user);
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Failed to remove account for " + user.getEmail(), e
            );
        }

        // Wipe out the session now that their account is gone
        session.invalidate();
        resp.sendRedirect("index.jsp");
    }

    @Override
    public void destroy() {
        try {
            if (dao != null) dao.close();
        } catch (SQLException ignored) {}
    }
}
