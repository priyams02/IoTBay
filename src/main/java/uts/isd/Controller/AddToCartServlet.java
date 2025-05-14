package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CartDBManager;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.OrderLineItem;
import uts.isd.model.Person.Customer;
import uts.isd.model.Product;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

@WebServlet(name = "AddToCart", urlPatterns = "/AddToCart")
public class AddToCartServlet extends IoTWebpageBase {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }

        ProductDBManager productDB = dao.products();
        CartDBManager    cartDB    = dao.cart();

        String pidStr = trim(request.getParameter("productID"));
        if (pidStr.isEmpty()) {
            redirectWithParam(response, "error", "No product specified");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(pidStr);
        } catch (NumberFormatException e) {
            redirectWithParam(response, "error", "Invalid product ID");
            return;
        }

        try {
            Product p = productDB.findProduct(String.valueOf(productId));
            if (p == null) {
                redirectWithParam(response, "error", "Product not found");
                return;
            }

            Customer owner = (Customer) session.getAttribute("loggedInUser");
            if (owner != null) {
                // Registered user's cart
                cartDB.listCart(owner.getEmail());        // ensure cart loaded
                cartDB.add(new OrderLineItem(p, owner, 1));
            } else {
                // Anonymous cart
                cartDB.listCart(DAO.AnonymousUserEmail);  // loads anonymous cart
                cartDB.add(new OrderLineItem(p, null, 1));
            }

            // Store updated cart in session
            session.setAttribute("cart", cartDB.listCart(
                    owner != null ? owner.getEmail() : DAO.AnonymousUserEmail
            ));
            redirectWithParam(response, "msg", "Added to cart");

        } catch (SQLException e) {
            throw new ServletException("Error adding to cart", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just show the cart
        request.getRequestDispatcher("IoTCore/Cart.jsp").forward(request, response);
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect back to cart page with URL-encoded param */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String enc = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Cart.jsp?" + key + "=" + enc);
    }
}
