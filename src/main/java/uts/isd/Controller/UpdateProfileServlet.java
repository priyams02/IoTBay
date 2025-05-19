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
@WebServlet(name = "UpdateProfileServlet", urlPatterns = "/UpdateProfile")
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
                // Update customer fields
                Customer cust = (Customer) session.getAttribute("loggedInUser");
                switch (attribute) {
                    case "Names":
                        cust.setFirstName(trim(request.getParameter("First")));
                        cust.setLastName(trim(request.getParameter("Last")));
                        break;
                    case "Email":
                        cust.setEmail(trim(request.getParameter("Email")).toLowerCase());
                        break;
                    case "PhoneNumber":
                        cust.setPhoneNumber(trim(request.getParameter("PhoneNumber")));
                        break;
                    case "Address":
                        Address addr = new Address(
                                trim(request.getParameter("addNum")),
                                trim(request.getParameter("addStreetName")),
                                trim(request.getParameter("addSuburb")),
                                trim(request.getParameter("addPostcode")),
                                trim(request.getParameter("addCity"))
                        );
                        cust.setAddress(addr);
                        break;
                    case "PaymentInformation":
                        PaymentInformation pi = new PaymentInformation(
                                trim(request.getParameter("CardNo")),
                                trim(request.getParameter("CVV")),
                                trim(request.getParameter("CardHolder")),
                                null
                        );
                        cust.setPaymentInfo(pi);
                        break;
                    default:
                        // No-op for other cases
                }

                // Persist changes
                customerDB.update(cust, cust);
                // Refresh session
                session.setAttribute("loggedInUser", cust);
                // Redirect back with success message
                response.sendRedirect(ctx + "/Profile.jsp?upd="
                        + URLEncoder.encode(attribute + " Updated!", StandardCharsets.UTF_8));
                return;
            }

            // Staff update names
            if ("Names".equals(attribute)) {
                Staff staff = (Staff) session.getAttribute("loggedInUser");
                staff.setFirstName(trim(request.getParameter("First")));
                staff.setLastName(trim(request.getParameter("Last")));
                staffDB.update(staff, staff);
                response.sendRedirect(ctx + "/Profile.jsp?upd=Names Updated!");
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