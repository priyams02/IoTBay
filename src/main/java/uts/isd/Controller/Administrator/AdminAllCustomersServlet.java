package uts.isd.Controller.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Servlet for administrators to view, remove, or update all customers.
 */
@WebServlet(name = "AdminAllCustomers", urlPatterns = "/AdminAllCustomers")
public class AdminAllCustomersServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to admin page
        request.getRequestDispatcher("IoTCore/Administrator/Admin.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        CustomerDBManager customerDB = dao.customers();

        String email = trim(request.getParameter("Email")).toLowerCase();
        boolean hasEdited = request.getParameter("bEdited") != null;

        try {
            // Attempt to find the customer
            Customer existing = customerDB.findCustomer(email);
            if (existing == null) {
                redirectWithParam(response, "error", "Customer not found");
                return;
            }

            if (request.getParameter("Remove") != null) {
                // Remove customer
                customerDB.delete(existing);
                redirectWithParam(response, "msg", "Removed " + existing.getFirstName() + " from IoTBay!");
            } else if (request.getParameter("Update") != null) {
                // Redirect to editor
                response.sendRedirect("IoTCore/Administrator/CustomerEditor.jsp?Email=" +
                        URLEncoder.encode(email, StandardCharsets.UTF_8));
            } else if (hasEdited) {
                // Process edit submission
                String fn  = onNull(request.getParameter("fn"), existing.getFirstName());
                String ln  = onNull(request.getParameter("ln"), existing.getLastName());
                String pw  = onNull(request.getParameter("pw"), existing.getPassword());
                String pn  = onNull(request.getParameter("pn"), existing.getPhoneNumber());
                String sNo = onNull(request.getParameter("sNo"), existing.getAddress().getNumber());
                String sNa = onNull(request.getParameter("sNa"), existing.getAddress().getStreetName());
                String sub = onNull(request.getParameter("sub"), existing.getAddress().getSuburb());
                String pc  = onNull(request.getParameter("pc"), existing.getAddress().getPostcode());
                String ct  = onNull(request.getParameter("ct"), existing.getAddress().getCity());
                String cno = onNull(request.getParameter("cno"), existing.getPaymentInfo().getCardNo());
                String cvv = onNull(request.getParameter("cvv"), existing.getPaymentInfo().getCVV());
                String cho = onNull(request.getParameter("cho"), existing.getPaymentInfo().getCardHolder());

                Address updatedAddr = new Address(sNo, sNa, sub, pc, ct);
                PaymentInformation updatedPay = new PaymentInformation(cno, cvv, cho, null);

                Customer updated = new Customer(fn, ln, pw, email, updatedAddr, pn, Customer.UserType.CUSTOMER);
                updated.setPaymentInfo(updatedPay);

                customerDB.update(existing, updated);
                redirectWithParam(response, "msg", "Updated Customer: " + fn + "");
            } else {
                // No recognized action
                redirectWithParam(response, "error", "No action specified");
            }
        } catch (SQLException e) {
            throw new ServletException("Error processing customer action", e);
        }
    }

    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String encoded = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Administrator/Admin.jsp?" + key + "=" + encoded);
    }

    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    private String onNull(String param, String fallback) {
        return (param == null || param.isEmpty()) ? fallback : param.trim();
    }
}
