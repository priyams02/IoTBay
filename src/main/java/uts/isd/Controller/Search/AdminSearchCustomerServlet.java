package uts.isd.controller.Search;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.*;
import uts.isd.model.Person.Customer;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for administrators to search for customers.
 */
@WebServlet(name = "AdminSearchCustomers", urlPatterns = "/AdminSearchCustomers")
public class AdminSearchCustomersServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to search results page
        request.getRequestDispatcher("IoTCore/Administrator/ViewAllSearchedCustomers.jsp")
                .forward(request, response);
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

        String params = trim(request.getParameter("Params"));
        if (params.isEmpty()) {
            redirectWithParam(response, "err", "Search term required");
            return;
        }

        try {
            List<Customer> results = customerDB.searchCustomers(params);
            session.setAttribute("searchedCustomers", results);
            // Forward for display
            request.getRequestDispatcher("IoTCore/Administrator/ViewAllSearchedCustomers.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Unable to search customers", e);
        }
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect with URL-encoded param */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String encoded = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Administrator/ViewAllSearchedCustomers.jsp?" + key + "=" + encoded);
    }
}
