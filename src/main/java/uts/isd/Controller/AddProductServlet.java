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
import java.util.List;

/**
 * Servlet for staff to add new products to IoTBay.
 */
@WebServlet(name = "AddProduct", urlPatterns = "/AddProduct")
public class AddProductServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the Add Product JSP form
        request.getRequestDispatcher("/AddProduct.jsp")
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
        ProductDBManager productDB = dao.products();

        // Retrieve and trim inputs
        String name = trim(request.getParameter("ProductName"));
        String desc = trim(request.getParameter("ProductDesc"));
        String priceStr = trim(request.getParameter("ProductPrice"));
        String quantStr = trim(request.getParameter("ProductQuant"));

        // Validate required fields
        if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty() || quantStr.isEmpty()) {
            redirectWithParam(response, "err", "All fields are required");
            return;
        }

        try {
            // Check for duplicate product name
            List<Product> existing = productDB.search(name);
            for (Product p : existing) {
                if (p.getName().equalsIgnoreCase(name)) {
                    redirectWithParam(response, "err", "A product with that name already exists!");
                    return;
                }
            }

            // Parse price and quantity
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantStr);

            // Create and persist product
            Product product = new Product(name, desc, price, quantity);
            productDB.add(product);

            // Success redirect
            redirectWithParam(response, "msg", name + " has been added to IoTBay!");

        } catch (NumberFormatException e) {
            redirectWithParam(response, "err", "Price and Quantity must be numeric");
        } catch (SQLException e) {
            throw new ServletException("Error adding product", e);
        }
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect with URL-encoded parameter */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String encoded = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("/AddProduct.jsp?" + key + "=" + encoded);
    }
}