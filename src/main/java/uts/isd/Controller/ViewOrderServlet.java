package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.DAO.DAO;
import uts.isd.model.Order;
import uts.isd.model.Person.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ViewOrderServlet", urlPatterns = "/Orders")
public class ViewOrderServlet extends HttpServlet {

    private DAO ensureDAO(HttpServletRequest req) throws ServletException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            try {
                dao = new DAO();
                session.setAttribute("dao", dao);
            } catch (SQLException e) {
                throw new ServletException("Failed to initialize DAO", e);
            }
        }
        return dao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        DAO dao = ensureDAO(req);

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        String email = null;
        if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            email = user.getEmail();
        } else {
            email = req.getParameter("email");
        }

        if (email == null || email.trim().isEmpty()) {
            req.setAttribute("err", "You must be logged in or provide an email to view orders.");
            req.getRequestDispatcher("orders.jsp").forward(req, resp);
            return;
        }

        try {
            String orderIdStr = req.getParameter("orderId");
            String orderDate = req.getParameter("orderDate");

            List<Order> orders;

            if (orderIdStr != null && !orderIdStr.trim().isEmpty()) {
                int orderId = Integer.parseInt(orderIdStr.trim());
                Order order = dao.orders().findOrder(orderId, email);
                orders = order != null ? List.of(order) : List.of();
            } else if (orderDate != null && !orderDate.trim().isEmpty()) {
                // Ensure date format is YYYY-MM-DD
                orders = dao.orders().findOrdersByDate(email, orderDate.trim());
            } else {
                orders = dao.orders().listByOwner(email);
            }

            req.setAttribute("orders", orders);
            req.setAttribute("userEmail", email);
            req.getRequestDispatcher("orders.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Failed to load orders", e);
        } catch (NumberFormatException e) {
            req.setAttribute("err", "Invalid Order ID format.");
            req.getRequestDispatcher("orders.jsp").forward(req, resp);
        }
    }
}
