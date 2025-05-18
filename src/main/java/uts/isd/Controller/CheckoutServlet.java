package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CartDBManager;
import uts.isd.model.DAO.OrderDBManager;
import uts.isd.model.Order;
import uts.isd.model.OrderLineItem;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.PaymentInformation;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Checkout", urlPatterns = {"/Checkout", "/Orders"})
public class CheckoutServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) throw new ServletException("DAO not initialized in session");

        if ("/Checkout".equals(path)) {
            // Show the checkout form
            request.getRequestDispatcher("/checkout.jsp")
                    .forward(request, response);
        } else {
            // "/Orders" → list past orders
            String email = "";
            Object u = session.getAttribute("loggedInUser");
            if (u != null) {
                email = ((uts.isd.model.Person.User)u).getEmail();
            } else {
                email = request.getParameter("email");
            }

            try {
                List<Order> orders = dao.orders().listByOwner(email);
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/orders.jsp")
                        .forward(request, response);
            } catch (SQLException e) {
                throw new ServletException("Failed to fetch orders", e);
            }
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Always handles "/Checkout" form submission
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) throw new ServletException("DAO not initialized in session");

        CartDBManager cartDB   = dao.cart();
        OrderDBManager orderDB = dao.orders();

        // 1) pull & trim form fields
        String email           = trim(request.getParameter("email"));
        String num             = trim(request.getParameter("addNum"));
        String street          = trim(request.getParameter("addStreetName"));
        String suburb          = trim(request.getParameter("addSuburb"));
        String postcode        = trim(request.getParameter("addPostcode"));
        String city            = trim(request.getParameter("addCity"));
        String cardNo          = trim(request.getParameter("CardNo"));
        String cvv             = trim(request.getParameter("CVV"));
        String cardHolder      = trim(request.getParameter("CardHolder"));

        // 2) validate
        if (email.isEmpty() || num.isEmpty() || street.isEmpty() ||
                suburb.isEmpty() || postcode.isEmpty() || city.isEmpty() ||
                cardNo.isEmpty() || cvv.isEmpty() || cardHolder.isEmpty()) {
            String err = URLEncoder.encode("All fields are required", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/checkout.jsp?err=" + err);
            return;
        }

        // 3) build Address + PaymentInformation
        Address shipping = new Address(num, street, suburb, postcode, city);
        PaymentInformation pi = new PaymentInformation(cardNo, cvv, cardHolder, null);

        // 4) load this user’s cart from the DB (fallback is session-cart)
        List<OrderLineItem> cart;
        try {
            cart = dao.cart().listCart(email);
        } catch (SQLException e) {
            throw new ServletException("Failed to load cart", e);
        }
        if (cart == null || cart.isEmpty()) {
            String err = URLEncoder.encode("Your cart is empty", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/checkout.jsp?err=" + err);
            return;
        }

        try {
            // 5) place the order (decrements stock & clears cart)
            Order newOrder = orderDB.makeOrder(email, cart, shipping, pi);

            // 6) save lastOrderId in session
            session.setAttribute("lastOrderId", newOrder.getId());

            // 7) drop any session-cached cart
            session.removeAttribute("cart");

            // 8) redirect to the orders list
            String msg = URLEncoder.encode("Order placed successfully", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/Orders?msg=" + msg);
        } catch (SQLException e) {
            throw new ServletException("Failed to place order", e);
        }
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }
}
