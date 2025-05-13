package uts.isd.Controller.Search;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.StaffDBManager;
import uts.isd.model.Person.Staff;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for administrators to search for staff by first name.
 */
@WebServlet(name = "AdminSearchStaff", urlPatterns = "/AdminSearchStaff")
public class AdminSearchStaffServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the JSP that will render the staff search form/results
        request.getRequestDispatcher("IoTCore/Administrator/ViewAllSearchedStaff.jsp")
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

        StaffDBManager staffDB = dao.staff();
        String params = trim(request.getParameter("Params"));
        if (params.isEmpty()) {
            redirectWithParam(response, "err", "Search term required");
            return;
        }

        try {
            // Use your StaffDBManager.searchStaff(...) method
            List<Staff> results = staffDB.searchStaff(params);
            session.setAttribute("searchedStaff", results);

            // Forward to JSP to display them
            request.getRequestDispatcher("IoTCore/Administrator/ViewAllSearchedStaff.jsp")
                    .forward(request, response);

        } catch (SQLException e) {
            throw new ServletException("Unable to search staff", e);
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
        response.sendRedirect("IoTCore/Administrator/ViewAllSearchedStaff.jsp?" + key + "=" + encoded);
    }
}
