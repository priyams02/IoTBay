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

@WebServlet(name = "ViewOrderServlet", urlPatterns = "/ViewOrders")
public class ViewOrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        User user = (User) session.getAttribute("loggedInUser");

        if (dao == null || user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            List<Order> orders = dao.orders().listByOwner(user.getEmail());
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Failed to load orders", e);
        }
    }
}
