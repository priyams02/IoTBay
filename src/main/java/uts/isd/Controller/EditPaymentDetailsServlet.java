package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.DAO.ShipmentDBManager;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.Person.User;
import uts.isd.model.Product;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for staff to add new products to IoTBay.
 */
@WebServlet("/EditPaymentDetailsServlet")
public class EditPaymentDetailsServlet extends IoTWebpageBase {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }

        String email           = trim(req.getParameter("email"));
        String addressNum      = trim(req.getParameter("addNum"));
        String addressStreet   = trim(req.getParameter("addStreetName"));
        String addressSuburb   = trim(req.getParameter("addSuburb"));
        String addressPostcode = trim(req.getParameter("addPostcode"));
        String addressCity     = trim(req.getParameter("addCity"));
        String cardNo          = trim(req.getParameter("CardNo"));
        String cvv             = trim(req.getParameter("CVV"));
        String cardHolder      = trim(req.getParameter("CardHolder"));
        CustomerDBManager customerDB = dao.customers();

        String error = validateCheckout(customerDB, email, addressNum, addressStreet, addressSuburb,
                addressPostcode, addressCity, cardNo, cvv, cardHolder);
        if (error != null) {
            req.setAttribute("err", error);
            req.getRequestDispatcher("/register.jsp")
                    .forward(req, resp);
            return;
        }
        try {
            Address address = new Address(addressNum, addressStreet, addressSuburb, addressPostcode, addressCity);
            Customer current = customerDB.findCustomer(email);
            PaymentInformation newPaymentInformation = new PaymentInformation(cardNo, cvv, cardHolder, null);
            Customer newCustomer = new Customer(
                    current.getFirstName(),
                    current.getLastName(),
                    current.getPassword(),
                    current.getEmail(),
                    address,
                    current.getPhoneNumber(),
                    newPaymentInformation,
                    User.UserType.CUSTOMER
            );
            customerDB.update(current, newCustomer);
            session.setAttribute("addNum", address);
            session.setAttribute("addStreetName", addressStreet);
            session.setAttribute("addSuburb", addressSuburb);
            session.setAttribute("addPostcode", addressPostcode);
            session.setAttribute("addCity", addressCity);
            session.setAttribute("cardNo", cardNo);
            session.setAttribute("CVV", cvv);
            session.setAttribute("cardHolder", cardHolder);
            resp.sendRedirect(req.getContextPath() + "/checkout.jsp");

        } catch (SQLException e) {
            throw new ServletException("Update failed", e);

        }
        
    }

    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }
    
    private String validateCheckout(CustomerDBManager customerDB, String email, String addressNum,
                                    String addressStreet, String addressSuburb, String addressPostcode,
                                    String addressCity, String cardNo, String cvv, String cardHolder) {

        if (Validator.containsLetter(addressNum)) {
            return "Address numbers must not contain letters!";
        }
        if (Validator.containsNumber(addressStreet)) {
            return "Street names must not contain numbers!";
        }
        if (Validator.containsNumber(addressSuburb)) {
            return "Suburb names must not contain numbers!";
        }
        if (Validator.containsLetter(addressPostcode)) {
            return "Postcode names must not contain letters!";
        }
        if (Validator.containsNumber(addressCity)) {
            return "City names must not contain numbers!";
        }
        if (Validator.containsLetter(cardNo)) {
            return "Card numbers must not contain letters!";
        }
        if (Validator.containsLetter(cvv)) {
            return "CVV must not contain letters!";
        }
        if (Validator.containsNumber(cardHolder)) {
            return "Card holder names must not contain numbers!";
        }
        return null;
    }
}