package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.DAO.StaffDBManager;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.Staff;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "Login", urlPatterns = "/Login")
public class LoginServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show login page
        request.getRequestDispatcher("IoTCore/Login.jsp")
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email    = trim(request.getParameter("Email")).toLowerCase();
        String password = trim(request.getParameter("Password"));

        if (email.isEmpty() || password.isEmpty()) {
            redirectWithParams(response,
                    "IoTCore/Login.jsp?",
                    new String[][] {
                            { "err",   "Email and Password required" },
                            { "Email", email }
                    }
            );
            return;
        }

        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }

        CustomerDBManager customerDB = dao.customers();
        StaffDBManager    staffDB    = dao.staff();

        try {
            // 1) Try customer login
            Customer customer = customerDB.findCustomer(email);
            if (customer != null && customer.getPassword().equals(password)) {
                session.setAttribute("loggedInUser", customer);
                redirectToRedirector(response,
                        "Welcome, " + customer.getFirstName() + "!",
                        "Please wait while we redirect you..."
                );
                return;
            }

            // 2) Try staff login
            Staff staff = staffDB.findStaff(email, password);
            if (staff != null) {
                session.setAttribute("loggedInUser", staff);
                redirectToRedirector(response,
                        "Welcome, " + staff.getFirstName() + "!",
                        "Redirecting to admin panel..."
                );
                return;
            }

            // 3) Hard-coded fallback admin
            if ("admin@iotbay.com".equals(email) && "admin".equals(password)) {
                response.sendRedirect("IoTCore/Administrator/Admin.jsp");
                return;
            }

            // 4) Failed authentication
            redirectWithParams(response,
                    "IoTCore/Login.jsp?",
                    new String[][] {
                            { "err",   "Incorrect E-Mail or Password!" },
                            { "Email", email }
                    }
            );

        } catch (SQLException e) {
            throw new ServletException("Login failed", e);
        }
    }

    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    private void redirectToRedirector(HttpServletResponse resp,
                                      String heading, String message) throws IOException {
        String url = "IoTCore/Redirector.jsp?HeadingMessage="
                + URLEncoder.encode(heading, StandardCharsets.UTF_8)
                + "&Message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(url);
    }

    private void redirectWithParams(HttpServletResponse resp,
                                    String baseUrl, String[][] params) throws IOException {
        StringBuilder sb = new StringBuilder(baseUrl);
        for (int i = 0; i < params.length; i++) {
            String key = params[i][0], val = params[i][1];
            if (i > 0) sb.append("&");
            sb.append(key).append("=")
                    .append(URLEncoder.encode(val, StandardCharsets.UTF_8));
        }
        resp.sendRedirect(sb.toString());
    }
}
