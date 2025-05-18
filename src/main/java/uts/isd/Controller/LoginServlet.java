package uts.isd.Controller;

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

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp")
                .forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ctx      = request.getContextPath();
        String email    = trim(request.getParameter("Email")).toLowerCase();
        String password = trim(request.getParameter("Password"));

        // 1) Required fields
        if (email.isEmpty() || password.isEmpty()) {
            response.sendRedirect(ctx + "/login.jsp?err=" +
                    URLEncoder.encode("Email and Password required", StandardCharsets.UTF_8) +
                    "&Email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));
            return;
        }

        // 2) Create DAO
        DAO dao;
        try {
            dao = new DAO();
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }

        CustomerDBManager customerDB = dao.customers();
        StaffDBManager    staffDB    = dao.staff();

        try {
            // 3) Customer login
            Customer customer = customerDB.findCustomer(email);
            if (customer != null && customer.getPassword().equals(password)) {
                HttpSession sess = request.getSession();
                sess.setAttribute("loggedInUser", customer);

                // ─── DEBUG: before logging ───────────────────────────
                System.out.println("DEBUG: login success for " + customer.getEmail());
                try {
                    System.out.println("DEBUG: calling addLog()");
                    dao.AccessLogs().addLog(customer.getEmail(), "LOGIN");
                    System.out.println("DEBUG: addLog() completed");
                } catch (SQLException e) {
                    System.err.println("DEBUG: SQLException in addLog():");
                    e.printStackTrace();
                }
                // ──────────────────────────────────────────────────────

                String heading = "Welcome, " + customer.getFirstName() + "!";
                String message = "Please wait while we redirect you...";
                response.sendRedirect(ctx + "/Redirector.jsp?HeadingMessage=" +
                        URLEncoder.encode(heading, StandardCharsets.UTF_8) +
                        "&Message=" +
                        URLEncoder.encode(message, StandardCharsets.UTF_8));
                return;
            }

            // 4) Staff login
            Staff staff = staffDB.findStaff(email, password);
            if (staff != null) {
                HttpSession sess = request.getSession();
                sess.setAttribute("loggedInUser", staff);

                // (Optional) you could also log staff logins if desired

                String heading = "Welcome, " + staff.getFirstName() + "!";
                String message = "Redirecting to admin panel...";
                response.sendRedirect(ctx + "/Redirector.jsp?HeadingMessage=" +
                        URLEncoder.encode(heading, StandardCharsets.UTF_8) +
                        "&Message=" +
                        URLEncoder.encode(message, StandardCharsets.UTF_8));
                return;
            }

            // 5) Hard-coded admin
            if ("admin@iotbay.com".equals(email) && "admin".equals(password)) {
                response.sendRedirect(ctx + "/IoTCore/Administrator/Admin.jsp");
                return;
            }

            // 6) Failed auth
            response.sendRedirect(ctx + "/login.jsp?err=" +
                    URLEncoder.encode("Incorrect E-Mail or Password!", StandardCharsets.UTF_8) +
                    "&Email=" + URLEncoder.encode(email, StandardCharsets.UTF_8));

        } catch (SQLException e) {
            throw new ServletException("Login failed", e);
        }
    }

    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }
}
