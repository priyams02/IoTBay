package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        String ctx = req.getContextPath();
        // Redirect to login if no session
        if (session == null) {
            resp.sendRedirect(ctx + "/LoginServlet");
            return;
        }

        // Retrieve logged-in user from "loggedInUser"
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            resp.sendRedirect(ctx + "/LoginServlet");
            return;
        }

        try {
            if (user.getType() == User.UserType.CUSTOMER) {
                Customer cust = (Customer) user;
                dao.customers().delete(cust);
            } else if (user.getType() == User.UserType.STAFF) {
                Staff staff = (Staff) user;
                dao.staff().delete(staff);
            }
        } catch (SQLException e) {
            throw new ServletException(
                    "Failed to remove account for " + user.getEmail(), e
            );
        }

        // Invalidate session after deletion
        session.invalidate();
        // Redirect back to home page
        resp.sendRedirect(ctx + "/index.jsp");
    }

    @Override
    public void destroy() {
        try {
            if (dao != null) dao.close();
        } catch (SQLException ignored) {}
    }
}
