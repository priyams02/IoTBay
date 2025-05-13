package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.DAO.CustomerDBManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Servlet for administrators to add new customers.
 */
@WebServlet(name = "AdminAddCustomerServlet", urlPatterns = "/AdminAddCustomer")
public class AdminAddCustomerServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Display admin interface
        request.getRequestDispatcher("IoTCore/Administrator/Admin.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve DAO from session
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        CustomerDBManager customerDB = dao.customers();

        // 1. Retrieve & trim inputs
        String fn  = trim(request.getParameter("fn"));
        String ln  = trim(request.getParameter("ln"));
        String em  = trim(request.getParameter("em")).toLowerCase();
        String pw  = trim(request.getParameter("pw"));
        String pn  = trim(request.getParameter("pn"));
        String sNo = trim(request.getParameter("sNo"));
        String sNa = trim(request.getParameter("sNa"));
        String sub = trim(request.getParameter("sub"));
        String pc  = trim(request.getParameter("pc"));
        String ct  = trim(request.getParameter("ct"));

        // 2. Validate required fields
        if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || pw.isEmpty()) {
            redirectWithParam(response, "error", "Missing required fields");
            return;
        }

        // 3. Build Address and Customer
        Address address = new Address(sNo, sNa, sub, pc, ct);
        PaymentInformation payment = new PaymentInformation();
        Customer customer = new Customer(fn, ln, pw, em, address, pn, Customer.UserType.CUSTOMER);
        customer.setPaymentInfo(payment);

        try {
            // 4. Check if already exists
            if (customerDB.findCustomer(em) != null) {
                redirectWithParam(response, "error", "Customer already exists");
                return;
            }
            // 5. Persist using CustomerDBManager
            customerDB.add(customer);

            // 6. Success redirect
            redirectWithParam(response, "msg", "Added " + fn + " as a Customer!");
        } catch (SQLException e) {
            throw new ServletException("Error adding customer", e);
        }
    }

    /** Helper to URL-encode and redirect */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String encoded = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Administrator/Admin.jsp?" + key + "=" + encoded);
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }
}