package uts.isd.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.Person.*;
import uts.isd.model.Product;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Servlet for administrators to delete products from IoTBay.
 */
@WebServlet(name = "DeleteProduct", urlPatterns = "/DeleteProduct")
public class DeleteProductServlet extends IoTWebpageBase {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        ProductDBManager productDB = dao.products();

        String productId = trim(request.getParameter("ProductID"));
        if (productId.isEmpty()) {
            redirectWithParam(response, "error", "Product ID is required");
            return;
        }

        try {
            Product p = productDB.findProduct(productId);
            if (p == null) {
                redirectWithParam(response, "error", "Product not found");
                return;
            }

            String name = p.getName();
            productDB.delete(p);

            // Redirect with success message
            String msg = URLEncoder.encode("Removed " + name + " successfully", StandardCharsets.UTF_8);
            response.sendRedirect("IoTCore/Redirector.jsp?HeadingMessage=" + msg +
                    "&Message=" + URLEncoder.encode("Please wait while we redirect you.", StandardCharsets.UTF_8));
        } catch (SQLException e) {
            throw new ServletException("Error deleting product", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the admin delete page
        request.getRequestDispatcher("IoTCore/StaffControlPanel/DeleteProduct.jsp").forward(request, response);
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect with URL-encoded param helper */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String enc = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/StaffControlPanel/DeleteProduct.jsp?" + key + "=" + enc);
    }
}