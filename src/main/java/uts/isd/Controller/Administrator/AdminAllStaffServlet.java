package uts.isd.Controller.Administrator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.StaffDBManager;
import uts.isd.model.Person.Staff;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Servlet for administrators to view, remove, or update staff members.
 */
@WebServlet(name = "AdminAllStaff", urlPatterns = "/AdminAllStaff")
public class AdminAllStaffServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to admin interface
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
        StaffDBManager staffDB = dao.staff();

        String email = trim(request.getParameter("Email")).toLowerCase();
        boolean edited = request.getParameter("bEdited") != null;

        try {
            Staff existing = staffDB.findStaff(email);
            if (existing == null) {
                redirectWithParam(response, "error", "Staff member not found");
                return;
            }

            if (request.getParameter("Remove") != null) {
                staffDB.delete(existing);
                redirectWithParam(response, "msg", "Removed " + existing.getFirstName() + " from IoTBay!");

            } else if (request.getParameter("Update") != null) {
                response.sendRedirect("IoTCore/Administrator/StaffEditor.jsp?Email=" +
                        URLEncoder.encode(email, StandardCharsets.UTF_8));

            } else if (edited) {
                // Process updates
                String fn = onNull(request.getParameter("fn"), existing.getFirstName());
                String ln = onNull(request.getParameter("ln"), existing.getLastName());
                String em = onNull(request.getParameter("em"), existing.getEmail()).toLowerCase();
                String pw = onNull(request.getParameter("pw"), existing.getPassword());

                Staff updated = new Staff(fn, ln, pw, em);
                staffDB.update(existing, updated);
                redirectWithParam(response, "msg", "Updated Staff Member: " + fn);

            } else {
                redirectWithParam(response, "error", "No action specified");
            }
        } catch (SQLException e) {
            throw new ServletException("Error processing staff action", e);
        }
    }

    /** URL-safe redirect helper */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String encoded = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Administrator/Admin.jsp?" + key + "=" + encoded);
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Fallback helper */
    private String onNull(String param, String fallback) {
        return (param == null || param.isEmpty()) ? fallback : param.trim();
    }
}