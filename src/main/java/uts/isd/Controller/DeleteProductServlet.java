package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
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
        String context = request.getContextPath() + "/ListProducts";
        if (productId.isEmpty()) {
            String err = URLEncoder.encode("Product ID is required", StandardCharsets.UTF_8);
            response.sendRedirect(context + "?err=" + err);
            return;
        }

        try {
            Product p = productDB.findProduct(productId);
            if (p == null) {
                String err = URLEncoder.encode("Product not found", StandardCharsets.UTF_8);
                response.sendRedirect(context + "?err=" + err);
                return;
            }

            String name = p.getName();
            productDB.delete(p);

            String msg = URLEncoder.encode("Removed " + name + " successfully", StandardCharsets.UTF_8);
            response.sendRedirect(context + "?msg=" + msg);
        } catch (SQLException e) {
            throw new ServletException("Error deleting product", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If someone tries GET, just send them back to the list
        response.sendRedirect(request.getContextPath() + "/ListProducts");
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }
}
