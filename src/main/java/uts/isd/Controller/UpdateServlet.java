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
import uts.isd.model.Product;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Servlet for updating customer, staff, and product records.
 */
@WebServlet(name = "Update", urlPatterns = "/Update")
public class UpdateServlet extends IoTWebpageBase {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        CustomerDBManager customerDB = dao.customers();
        StaffDBManager    staffDB    = dao.staff();
        ProductDBManager  productDB  = dao.products();

        String firstName = trim(request.getParameter("First"));
        String lastName  = trim(request.getParameter("Last"));
        String email     = trim(request.getParameter("Email")).toLowerCase();

        // Redirect to customer edit form
        if (request.getParameter("Update") != null) {
            redirect(response,
                    "IoTCore/StaffControlPanel/CustomerProfileUpdator.jsp?Email=" +
                            URLEncoder.encode(email, StandardCharsets.UTF_8)
            );
            return;
        }

        // Remove customer
        if (request.getParameter("Remove") != null) {
            try {
                Customer c = customerDB.findCustomer(email);
                if (c != null) customerDB.delete(c);
                redirectWithParam(response,
                        "IoTCore/StaffControlPanel/SeeEditCustomers.jsp?",
                        "upd","Customer Removed!"
                );
            } catch (SQLException e) {
                throw new ServletException("Failed to remove customer", e);
            }
            return;
        }

        // Common params
        String attribute = trim(request.getParameter("Attribute"));
        boolean isCustomer   = "yes".equals(request.getParameter("bIsCustomer"));
        boolean fromStaff    = request.getParameter("CalledFromStaff") != null;
        String redirectBase  = fromStaff
                ? "IoTCore/StaffControlPanel/SeeEditCustomers.jsp?"
                : "IoTCore/Profile.jsp?";

        try {
            if (isCustomer) {
                // Determine which customer to update
                String keyEmail = fromStaff
                        ? trim(request.getParameter("originalEmail")).toLowerCase()
                        : ((Customer)session.getAttribute("loggedInUser")).getEmail();
                Customer current = customerDB.findCustomer(keyEmail);
                if (current == null) {
                    redirectWithParam(response, redirectBase, "err","Customer not found");
                    return;
                }
                // Fetch address and payment
                Address a = current.getAddress();
                PaymentInformation p = current.getPaymentInfo();
                // Build updated customer template
                Customer updated = new Customer(
                        firstName.isEmpty() ? current.getFirstName() : firstName,
                        lastName.isEmpty()  ? current.getLastName()  : lastName,
                        current.getPassword(),
                        email.isEmpty()      ? current.getEmail()     : email,
                        a, current.getPhoneNumber(), Customer.UserType.CUSTOMER
                );
                updated.setPaymentInfo(p);

                switch (attribute) {
                    case "Names":
                        customerDB.update(current, updated);
                        break;
                    case "Password":
                        String pw1 = trim(request.getParameter("Pass1"));
                        String pw2 = trim(request.getParameter("Pass2"));
                        if (!pw1.equals(pw2)) {
                            redirectWithParam(response, redirectBase, "err","Passwords did not match"); return;
                        }
                        updated.setPassword(pw1);
                        customerDB.update(current, updated);
                        break;
                    case "Email":
                        if (customerDB.findCustomer(updated.getEmail()) != null) {
                            redirectWithParam(response, redirectBase, "err","Email in use"); return;
                        }
                        customerDB.update(current, updated);
                        break;
                    case "PhoneNumber":
                        updated.setPhoneNumber(trim(request.getParameter("PhoneNumber")));
                        customerDB.update(current, updated);
                        break;
                    case "Address":
                        Address na = new Address(
                                trim(request.getParameter("addNum")),
                                trim(request.getParameter("addStreetName")),
                                trim(request.getParameter("addSuburb")),
                                trim(request.getParameter("addPostcode")),
                                trim(request.getParameter("addCity"))
                        );
                        updated = new Customer(
                                updated.getFirstName(), updated.getLastName(), updated.getPassword(),
                                updated.getEmail(), na, updated.getPhoneNumber(), Customer.UserType.CUSTOMER
                        );
                        updated.setPaymentInfo(p);
                        customerDB.update(current, updated);
                        break;
                    case "PaymentInformation":
                        PaymentInformation np = new PaymentInformation(
                                trim(request.getParameter("CardNo")),
                                trim(request.getParameter("CVV")),
                                trim(request.getParameter("CardHolder")),
                                null
                        );
                        updated.setPaymentInfo(np);
                        customerDB.update(current, updated);
                        break;
                    case "Product":
                        // Product update handled below for staff
                        break;
                    default:
                        // No-op
                }
                if (!attribute.equals("Product")) {
                    // Refresh session if customer
                    if (!fromStaff) session.setAttribute("loggedInUser", updated);
                    redirectWithParam(response, redirectBase, "upd", attribute + " Updated!");
                    return;
                }
            }

            // Staff branch or product update
            String attr = attribute;
            if ("Product".equals(attr) || !isCustomer) {
                // Product update case
                String pidStr = trim(request.getParameter("pid"));
                int pid = Integer.parseInt(pidStr);
                Product prod = productDB.findProduct(String.valueOf(pid));
                String name = trim(request.getParameter("ProductName"));
                String desc = trim(request.getParameter("ProductDesc"));
                double price = Double.parseDouble(trim(request.getParameter("ProductPrice")));
                int stock = Integer.parseInt(trim(request.getParameter("ProductStock")));
                Product updatedP = new Product(
                        prod.getProductId(), name, prod.getCategory(), price, stock
                );
                productDB.update(prod, updatedP);
                redirectToRedirector(response,
                        name + " Updated!",
                        "Please wait while we redirect you..."
                );
                return;
            }

        } catch (SQLException e) {
            throw new ServletException("Error updating record", e);
        }
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect helper */
    private void redirect(HttpServletResponse resp, String url) throws IOException {
        resp.sendRedirect(url);
    }

    /** Redirect with one param */
    private void redirectWithParam(HttpServletResponse resp, String base,
                                   String key, String val) throws IOException {
        String url = base + (base.contains("?") ? "&" : "?")
                + key + "=" + URLEncoder.encode(val, StandardCharsets.UTF_8);
        resp.sendRedirect(url);
    }

    /** Redirect to Redirector.jsp */
    private void redirectToRedirector(HttpServletResponse resp,
                                      String heading, String message) throws IOException {
        String url = "IoTCore/Redirector.jsp?HeadingMessage="
                + URLEncoder.encode(heading, StandardCharsets.UTF_8)
                + "&Message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(url);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Not used; all updates via POST
        resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }
}
