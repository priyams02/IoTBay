package uts.isd.Controller;

import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.User;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CustomerDBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends IoTWebpageBase {

    // 1) GET shows the form
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // forward to /register.jsp (in webapp root)
        req.getRequestDispatcher("/register.jsp")
                .forward(req, resp);
    }

    // 2) POST processes the form
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);

        // pull parameters
        String firstName = req.getParameter("First"),
                lastName  = req.getParameter("Last"),
                email     = req.getParameter("Email"),
                phone     = req.getParameter("PhoneNumber"),
                pass1     = req.getParameter("Pass1"),
                pass2     = req.getParameter("Pass2"),
                num       = req.getParameter("addNum"),
                street    = req.getParameter("addStreetName"),
                suburb    = req.getParameter("addSuburb"),
                postcode  = req.getParameter("addPostcode"),
                city      = req.getParameter("addCity");

        // init DAO
        DAO dao;
        try {
            dao = new DAO();
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
        CustomerDBManager customerDB = dao.customers();

        // validate
        String error = validateRegistration(
                customerDB,
                firstName, lastName, email, phone,
                pass1, pass2, num, street, suburb, postcode, city
        );
        if (error != null) {
            // on error, stash values & message, then forward back to JSP
            req.setAttribute("err", error);
            req.setAttribute("First", firstName);
            req.setAttribute("Last",  lastName);
            req.setAttribute("Email", email);
            // …repeat for PhoneNumber, addNum, etc…
            req.getRequestDispatcher("/register.jsp")
                    .forward(req, resp);
            return;
        }

        // build & save new customer
        Customer newCustomer = new Customer(
                firstName,
                lastName,
                pass1,
                email,
                new Address(num, street, suburb, postcode, city),
                phone,
                User.UserType.CUSTOMER
        );
        try {
            customerDB.add(newCustomer);
        } catch (SQLException e) {
            throw new ServletException("Error registering user", e);
        }

        // on success: login & redirect
        HttpSession session = req.getSession();
        session.setAttribute("loggedInUser", newCustomer);
        String ctx = req.getContextPath();
        String heading = "Welcome, " + firstName + "!";
        String message = "Thank you for registering with IoTBay!";
        resp.sendRedirect(ctx + "/Redirector.jsp?HeadingMessage="
                + heading + "&Message=" + message);
    }

    /**
     * Returns an error message if any field is invalid, or null if all good.
     */
    private String validateRegistration(
            CustomerDBManager customerDB,
            String firstName, String lastName, String email, String phone,
            String pass1, String pass2, String num,
            String street, String suburb, String postcode, String city) {

        try {
            if (customerDB.findCustomer(email) != null) {
                return "An account with that E-Mail already exists!";
            }
        } catch (SQLException ignored) {}

        if (!pass1.equals(pass2)) {
            return "Passwords did not match!";
        }
        if (Validator.containsNumber(firstName + lastName)) {
            return "Names cannot contain numbers!";
        }
        if (Validator.containsLetter(phone)) {
            return "Phone numbers cannot contain letters!";
        }
        if (num.length() >= 10 || Validator.containsLetter(num)) {
            return "Invalid street number!";
        }
        if (Validator.containsNumber(street)) {
            return "Street names cannot contain numbers!";
        }
        if (Validator.containsNumber(suburb)) {
            return "Suburb names cannot contain numbers!";
        }
        if (Validator.containsNumber(city)) {
            return "City names cannot contain numbers!";
        }
        if (Validator.containsLetter(postcode)) {
            return "Australian postcodes do not contain letters!";
        }
        return null;
    }
}
