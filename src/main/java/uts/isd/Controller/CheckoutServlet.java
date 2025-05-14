package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
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

@WebServlet(name = "Checkout", urlPatterns = "/Checkout")
public class CheckoutServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // show checkout form
        request.getRequestDispatcher("/checkout.jsp")
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

        CartDBManager cartDB   = dao.cart();
        OrderDBManager orderDB = dao.orders();

        // 1) pull & trim
        String email           = trim(request.getParameter("email"));
        String addressNum      = trim(request.getParameter("addNum"));
        String addressStreet   = trim(request.getParameter("addStreetName"));
        String addressSuburb   = trim(request.getParameter("addSuburb"));
        String addressPostcode = trim(request.getParameter("addPostcode"));
        String addressCity     = trim(request.getParameter("addCity"));
        String cardNo          = trim(request.getParameter("CardNo"));
        String cvv             = trim(request.getParameter("CVV"));
        String cardHolder      = trim(request.getParameter("CardHolder"));

        // 2) basic validation
        if (email.isEmpty() ||
                addressNum.isEmpty() || addressStreet.isEmpty() ||
                addressSuburb.isEmpty() || addressPostcode.isEmpty() ||
                addressCity.isEmpty() ||
                cardNo.isEmpty() || cvv.isEmpty() || cardHolder.isEmpty()) {
            // redirect back with error
            String err = URLEncoder.encode("All fields are required", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/checkout.jsp?err=" + err);
            return;
        }

        // 3) build Address + PaymentInformation
        Address shipping = new Address(
                addressNum, addressStreet, addressSuburb, addressPostcode, addressCity
        );
        PaymentInformation pi = new PaymentInformation(cardNo, cvv, cardHolder, null);

        // 4) load cart from session
        @SuppressWarnings("unchecked")
        List<OrderLineItem> cart = (List<OrderLineItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            String err = URLEncoder.encode("Your cart is empty", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/checkout.jsp?err=" + err);
            return;
        }

        try {
            // 5) create order
            uts.isd.model.Order newOrder = orderDB.makeOrder(email, cart, shipping, pi);

            // 6) save lastOrderId in session for the nav
            session.setAttribute("lastOrderId", newOrder.getId());

            // 7) clear cart both DB & session
            cartDB.clear(email);
            session.removeAttribute("cart");

            // 8) redirect to orders.jsp (or wherever you list orders)
            String msg = URLEncoder.encode("Order placed successfully", StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/orders.jsp?msg=" + msg);
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
