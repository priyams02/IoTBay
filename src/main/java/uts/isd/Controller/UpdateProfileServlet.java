package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.DAO.StaffDBManager;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.Person.Staff;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Servlet for updating customer and staff profiles.
 */
@WebServlet(name = "UpdateProfileServlet", urlPatterns = {"/UpdateProfile", "/Update"})
public class UpdateProfileServlet extends IoTWebpageBase {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String ctx = request.getContextPath();
        if (session == null) {
            response.sendRedirect(ctx + "/LoginServlet");
            return;
        }

        // Initialize DAO and managers
        DAO dao;
        CustomerDBManager customerDB;
        StaffDBManager    staffDB;
        ProductDBManager  productDB;
        try {
            dao = new DAO();
            customerDB = dao.customers();
            staffDB    = dao.staff();
            productDB  = dao.products();
        } catch (SQLException e) {
            throw new ServletException("Unable to initialize data access", e);
        }

        String attribute = trim(request.getParameter("Attribute"));
        boolean isCustomer = "yes".equals(request.getParameter("bIsCustomer"));

        try {
            if (isCustomer) {
                // Retrieve original from DB to preserve old email
                Customer original = (Customer) session.getAttribute("loggedInUser");
                original = customerDB.findCustomer(original.getEmail());
                // Build updated copy
                String fn = trim(request.getParameter("First"));
                String ln = trim(request.getParameter("Last"));
                String email = trim(request.getParameter("Email")).toLowerCase();
                String phone = trim(request.getParameter("PhoneNumber"));
                Address addr = original.getAddress();
                PaymentInformation pay = original.getPaymentInfo();
                Customer updated = new Customer(
                        fn.isEmpty()    ? original.getFirstName()  : fn,
                        ln.isEmpty()    ? original.getLastName()   : ln,
                        original.getPassword(),
                        email.isEmpty() ? original.getEmail()      : email,
                        addr,
                        phone.isEmpty()? original.getPhoneNumber(): phone,
                        Customer.UserType.CUSTOMER
                );
                updated.setPaymentInfo(pay);

                // Handle other attributes
                if ("Address".equals(attribute)) {
                    Address a = new Address(
                            trim(request.getParameter("addNum")),
                            trim(request.getParameter("addStreetName")),
                            trim(request.getParameter("addSuburb")),
                            trim(request.getParameter("addPostcode")),
                            trim(request.getParameter("addCity"))
                    );
                    updated.setAddress(a);
                } else if ("PaymentInformation".equals(attribute)) {
                    PaymentInformation pi = new PaymentInformation(
                            trim(request.getParameter("CardNo")),
                            trim(request.getParameter("CVV")),
                            trim(request.getParameter("CardHolder")),
                            null
                    );
                    updated.setPaymentInfo(pi);
                }

                // Persist changes using old email key
                customerDB.update(original, updated);
                // Refresh session under both attributes
                session.setAttribute("loggedInUser", updated);
                session.setAttribute("User", updated);
                // Redirect back with success
                response.sendRedirect(ctx + "/Profile?upd="
                        + URLEncoder.encode(attribute + " Updated!", StandardCharsets.UTF_8));
                return;
            }

            // Staff update names
            if ("Names".equals(attribute)) {
                Staff staff = (Staff) session.getAttribute("loggedInUser");
                staff.setFirstName(trim(request.getParameter("First")));
                staff.setLastName(trim(request.getParameter("Last")));
                staffDB.update(staff, staff);
                session.setAttribute("User", staff);
                session.setAttribute("loggedInUser", staff);
                response.sendRedirect(ctx + "/Profile?upd=Names Updated!");
                return;
            }

            // Product updates for staff (if needed)
            // …

        } catch (SQLException e) {
            throw new ServletException("Failed to update profile: " + e.getMessage(), e);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    /** Helper for null-safe trim */
    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
