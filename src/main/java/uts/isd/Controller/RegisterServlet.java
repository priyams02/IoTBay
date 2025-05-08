package uts.isd.Controller;

import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;
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

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.doPost(request, response);

        // Pull form parameters
        String firstName = request.getParameter("First"),
                lastName  = request.getParameter("Last"),
                email     = request.getParameter("Email"),
                phone     = request.getParameter("PhoneNumber"),
                pass1     = request.getParameter("Pass1"),
                pass2     = request.getParameter("Pass2"),
                num       = request.getParameter("addNum"),
                street    = request.getParameter("addStreetName"),
                suburb    = request.getParameter("addSuburb"),
                postcode  = request.getParameter("addPostcode"),
                city      = request.getParameter("addCity");

        // Initialize DAO & CustomerDBManager
        DAO dao;
        try {
            dao = new DAO();
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
        CustomerDBManager customerDB = dao.customers();

        // Validate
        String error = validateRegistration(
                customerDB,
                firstName, lastName, email, phone,
                pass1, pass2, num, street, suburb, postcode, city
        );
        if (error != null) {
            String params = redirectParams(
                    "err", error,
                    "First", firstName,
                    "Last", lastName,
                    "Email", email,
                    "PhoneNumber", phone,
                    "addNum", num,
                    "addStreetName", street,
                    "addSuburb", suburb,
                    "addPostcode", postcode,
                    "addCity", city
            );
            response.sendRedirect("IoTCore/Register.jsp?" + params);
            return;
        }

        // Build new Customer object
        Customer newCustomer = new Customer(
                firstName,
                lastName,
                pass1,
                email,
                new Address(num, street, suburb, postcode, city),
                phone,
                User.UserType.CUSTOMER
        );

        // Persist via CustomerDBManager
        try {
            customerDB.add(newCustomer);
        } catch (SQLException e) {
            throw new ServletException("Error registering user", e);
        }

        // On success: store in session and redirect
        HttpSession session = request.getSession();
        session.setAttribute("User", newCustomer);
        String redirect = redirectParams(
                "HeadingMessage", "Welcome, " + firstName + "!",
                "Message", "Thank you for registering with IoTBay! Please wait while we redirect you..."
        );
        response.sendRedirect("IoTCore/Redirector.jsp?" + redirect);
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
