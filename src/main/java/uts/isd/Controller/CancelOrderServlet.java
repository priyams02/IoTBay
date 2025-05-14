package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.OrderDBManager;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * Servlet to cancel an order (registered or anonymous) and restore stock.
 */
@WebServlet(name = "CancelOrder", urlPatterns = "/CancelOrder")
public class CancelOrderServlet extends IoTWebpageBase {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        OrderDBManager orderDB = dao.orders();

        String oidParam = trim(request.getParameter("oid"));
        if (oidParam.isEmpty()) {
            redirectWithParam(response, "error", "Order ID is required");
            return;
        }

        int orderId;
        try {
            orderId = Integer.parseInt(oidParam);
        } catch (NumberFormatException e) {
            redirectWithParam(response, "error", "Invalid order ID");
            return;
        }

        String owner = trim(request.getParameter("owner"));
        if (owner.isEmpty()) {
            redirectWithParam(response, "error", "Owner email is required");
            return;
        }

        try {
            orderDB.cancelOrder(orderId, owner);
            redirectWithParam(response, "upd", "Cancelled Order!");
        } catch (SQLException e) {
            throw new ServletException("Error cancelling order", e);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to orders page
        request.getRequestDispatcher("IoTCore/Orders.jsp").forward(request, response);
    }

    /** Null-safe trim */
    private String trim(String s) {
        return (s == null ? "" : s.trim());
    }

    /** Redirect with URL-encoded param */
    private void redirectWithParam(HttpServletResponse response, String key, String val)
            throws IOException {
        String enc = URLEncoder.encode(val, StandardCharsets.UTF_8);
        response.sendRedirect("IoTCore/Orders.jsp?" + key + "=" + enc);
    }
}