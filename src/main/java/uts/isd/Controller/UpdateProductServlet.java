package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.Product;
import uts.isd.model.Person.User;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "UpdateProduct", urlPatterns = "/UpdateProduct")
public class UpdateProductServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DAO dao = (DAO) session.getAttribute("dao");
        ProductDBManager productDB = dao.products();

        String productId = request.getParameter("id");
        if (productId == null) {
            response.sendRedirect("ListProducts");
            return;
        }

        try {
            Product product = productDB.findProduct(productId);
            if (product == null) {
                redirectWithParam(response, "err", "Product not found.");
                return;
            }
            request.setAttribute("product", product);
            request.getRequestDispatcher("/editProduct.jsp")
                    .forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load product for editing", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        DAO dao = (DAO) session.getAttribute("dao");
        ProductDBManager productDB = dao.products();

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getType() != User.UserType.STAFF) {
            response.sendRedirect("/index.jsp");
            return;
        }

        // Retrieve updated fields
        String productId = request.getParameter("ProductId");
        String name = trim(request.getParameter("ProductName"));
        String category = trim(request.getParameter("ProductCategory"));
        String priceStr = trim(request.getParameter("ProductPrice"));
        String stockStr = trim(request.getParameter("ProductStock"));

        if (productId == null || name.isEmpty() || category.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            redirectWithParam(response, "err", "All fields are required.");
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            int stock = Integer.parseInt(stockStr);

            Product oldProduct = productDB.findProduct(productId);
            if (oldProduct == null) {
                redirectWithParam(response, "err", "Product not found.");
                return;
            }

            Product updatedProduct = new Product(productId, name, category, price, stock);
            productDB.update(oldProduct, updatedProduct);

            redirectWithParam(response, "msg", "Product updated successfully.");
        } catch (NumberFormatException e) {
            redirectWithParam(response, "err", "Price and stock must be numeric.");
        } catch (SQLException e) {
            throw new ServletException("Error updating product", e);
        }
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }

    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String encoded = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("ListProducts?" + key + "=" + encoded);
    }
}
