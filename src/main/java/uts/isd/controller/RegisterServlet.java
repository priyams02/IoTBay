package uts.isd.controller;

import uts.isd.model.Person.Customer;
import uts.isd.model.Person.User;
import uts.isd.model.DAO.DBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Simply show the registration form
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        DBManager db = (DBManager) session.getAttribute("db");

        // 1. Retrieve & trim inputs
        String firstName = trim(req.getParameter("firstName"));
        String lastName  = trim(req.getParameter("lastName"));
        String email     = trim(req.getParameter("email"));
        String password  = trim(req.getParameter("password"));
        boolean agreed   = req.getParameter("tos") != null;

        // 2. Validate required fields and TOS
        if (!agreed
                || firstName.isEmpty()
                || lastName.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                {
            resp.sendRedirect("register.jsp?error=missing");
            return;
        }

        try {
            // 3. Check if email already registered
            if (db.findCustomer(email.toLowerCase()) != null) {
                resp.sendRedirect("register.jsp?error=exists");
                return;
            }

            // 4. Create & persist new customer
            Customer customer = new Customer(
                    firstName,
                    lastName,
                    password,
                    email,
                    User.UserType.CUSTOMER
            );
            db.add(customer);

            // 5. Log them in
            session.setAttribute("loggedInUser", customer);
            session.setAttribute("name", firstName);

            // 6. Success → welcome page
            resp.sendRedirect("welcome.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("register.jsp?error=db");
        }
    }

    /** Helper to avoid NPE and strip whitespace */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }
}
