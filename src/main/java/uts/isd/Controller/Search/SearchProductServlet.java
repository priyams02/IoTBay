package uts.isd.Controller.Search;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.Product;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for searching products by name.
 */
@WebServlet(name = "SearchProduct", urlPatterns = "/SearchProduct")
public class SearchProductServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to listing page for initial display or previous results
        request.getRequestDispatcher("/Listing.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        ProductDBManager productDB = dao.products();

        String params = trim(request.getParameter("Params"));
        if (params.isEmpty()) {
            redirectWithParam(response, "err", "Search term required");
            return;
        }

        try {
            // Use ProductDBManager.search(...) method
            List<Product> results = productDB.search(params);
            session.setAttribute("searchedProducts", results);

            // Redirect to listing page with the search parameter
            String encoded = URLEncoder.encode(params, StandardCharsets.UTF_8);
            response.sendRedirect("/Listing.jsp?param=" + encoded);
        } catch (SQLException e) {
            throw new ServletException("Unable to search products", e);
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
        response.sendRedirect("/Listing.jsp?" + key + "=" + encoded);
    }
}
