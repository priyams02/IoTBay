package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CartDBManager;
import uts.isd.model.DAO.OrderDBManager;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.OrderLineItem;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Processes checkout: converts the session cart into an order (registered or anonymous).
 */
@WebServlet(name = "Checkout", urlPatterns = "/Checkout")
public class CheckoutServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Show the checkout form
        request.getRequestDispatcher("IoTCore/Checkout.jsp").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        // Managers for cart and orders
        CartDBManager cartDB = dao.cart();
        OrderDBManager orderDB = dao.orders();

        // 1. Retrieve & trim inputs
        String email       = trim(request.getParameter("email"));
        String isCustomer  = trim(request.getParameter("isCustomer"));
        String addressNum  = trim(request.getParameter("addNum"));
        String addressStreet = trim(request.getParameter("addStreetName"));
        String addressSuburb = trim(request.getParameter("addSuburb"));
        String addressPostcode = trim(request.getParameter("addPostcode"));
        String addressCity = trim(request.getParameter("addCity"));
        String cardNo      = trim(request.getParameter("CardNo"));
        String cvv         = trim(request.getParameter("CVV"));
        String cardHolder  = trim(request.getParameter("CardHolder"));

        // 2. Basic validation
        if (isCustomer.isEmpty() || email.isEmpty()
                || addressNum.isEmpty() || addressStreet.isEmpty()
                || addressSuburb.isEmpty() || addressPostcode.isEmpty()
                || addressCity.isEmpty()
                || cardNo.isEmpty() || cvv.isEmpty() || cardHolder.isEmpty()) {
            redirectWithParam(response, "err", "All fields are required");
            return;
        }

        // 3. Build address and payment info
        Address shipping = new Address(
                addressNum,
                addressStreet,
                addressSuburb,
                addressPostcode,
                addressCity
        );
        PaymentInformation pi = new PaymentInformation(cardNo, cvv, cardHolder,null);

        // 4. Retrieve cart from session or DB
        List<OrderLineItem> cart = (List<OrderLineItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectWithParam(response, "err", "There is nothing in your Cart!");
            return;
        }

        try {
            // 5. Persist the order
            orderDB.makeOrder(email, cart, shipping, pi);

            // 6. Clear cart in DB and session
            cartDB.clear(email);
            session.removeAttribute("cart");

            redirectWithParam(response, "msg", "Order placed successfully");
        } catch (SQLException e) {
            throw new ServletException("Failed to place order", e);
        }
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect with URL-encoded param to Orders page */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String enc = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Orders.jsp?" + key + "=" + enc);
    }
}