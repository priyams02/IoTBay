package uts.isd.Controller;

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

/**
 * Servlet to handle displaying and processing the product edit form.
 */
@WebServlet(name = "ProductEditor", urlPatterns = "/ProductEditor")
public class ProductEditorServlet extends IoTWebpageBase {

    /**
     * Handles GET requests by loading the specified product and forwarding to the edit JSP.
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        ProductDBManager productDB = dao.products();
        String pid = trim(request.getParameter("pid"));
        if (pid.isEmpty()) {
            // No ID provided, redirect to product listing
            response.sendRedirect("IoTCore/StaffControlPanel/ManageProducts.jsp");
            return;
        }
        try {
            Product product = productDB.findProduct(pid);
            if (product == null) {
                // Product not found
                response.sendRedirect(
                        "IoTCore/Redirector.jsp?HeadingMessage=" +
                                URLEncoder.encode("Product Not Found", StandardCharsets.UTF_8) +
                                "&Message=" +
                                URLEncoder.encode("No product found for ID " + pid, StandardCharsets.UTF_8)
                );
                return;
            }
            request.setAttribute("product", product);
            request.getRequestDispatcher("IoTCore/StaffControlPanel/EditProduct.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving product", e);
        }
    }

    /**
     * Handles POST requests by processing the submitted edit form and updating the product.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        ProductDBManager productDB = dao.products();
        String pid = trim(request.getParameter("pid"));
        String name = trim(request.getParameter("ProductName"));
        String category = trim(request.getParameter("ProductCat"));
        String priceStr = trim(request.getParameter("ProductPrice"));
        String stockStr = trim(request.getParameter("ProductStock"));

        if (pid.isEmpty() || name.isEmpty() || category.isEmpty()
                || priceStr.isEmpty() || stockStr.isEmpty()) {
            // Missing required fields: return to edit form with error
            redirectWithParam(response, "err", "All fields are required");
            return;
        }
        try {
            Product oldProduct = productDB.findProduct(pid);
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);
            Product updated = new Product(
                    oldProduct.getProductId(), name, category, price, stock
            );
            productDB.update(oldProduct, updated);
            redirectToRedirector(response,
                    name + " Updated!",
                    "Please wait while we redirect you...");
        } catch (SQLException | NumberFormatException e) {
            throw new ServletException("Error updating product", e);
        }
    }

    /** Null-safe trim helper. */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect back to the edit JSP with an encoded parameter. */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String enc = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect(
                "IoTCore/StaffControlPanel/EditProduct.jsp?" + key + "=" + enc
        );
    }

    /** Redirect to the centralized redirector page. */
    private void redirectToRedirector(HttpServletResponse resp,
                                      String heading, String message) throws IOException {
        String url = "IoTCore/Redirector.jsp?HeadingMessage="
                + URLEncoder.encode(heading, StandardCharsets.UTF_8)
                + "&Message=" + URLEncoder.encode(message, StandardCharsets.UTF_8);
        resp.sendRedirect(url);
    }
}
