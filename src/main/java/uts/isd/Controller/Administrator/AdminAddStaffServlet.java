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
 * Servlet for administrators to add new staff members.
 */
@WebServlet(name = "AdminAddStaff", urlPatterns = "/AdminAddStaff")
public class AdminAddStaffServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the admin interface
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

        // 1. Retrieve & trim inputs
        String fn = trim(request.getParameter("fn"));
        String ln = trim(request.getParameter("ln"));
        String em = trim(request.getParameter("em")).toLowerCase();
        String pw = trim(request.getParameter("pw"));

        // 2. Validate required fields
        if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || pw.isEmpty()) {
            redirectWithParam(response, "error", "Missing required fields");
            return;
        }

        // 3. Build Staff object
        Staff staff = new Staff(fn, ln, pw, em);

        try {
            // 4. Check if already exists
            if (staffDB.findStaff(em) != null) {
                redirectWithParam(response, "error", "Staff already exists");
                return;
            }
            // 5. Persist using StaffDBManager
            staffDB.add(staff);

            // 6. Success redirect
            redirectWithParam(response, "msg", "Added " + fn + " as a Staff Member!");
        } catch (SQLException e) {
            throw new ServletException("Error adding staff", e);
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