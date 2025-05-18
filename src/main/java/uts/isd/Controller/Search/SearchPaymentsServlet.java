package uts.isd.Controller.Search;

import uts.isd.model.DAO.DAO;
import uts.isd.model.Order;
import uts.isd.model.Person.Customer;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "SearchPaymentsServlet", urlPatterns = {"/paymentHistory"})
public class SearchPaymentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Customer customer = (Customer) session.getAttribute("loggedInUser"); // Retrieve the logged-in customer

        // Ensure the customer is logged in
        if (customer == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Get search parameters
        String searchId = request.getParameter("searchId");
        String searchDate = request.getParameter("searchDate");

        // Retrieve customer purchase history
        ArrayList<Order> purchaseHistory = new ArrayList<>(customer.getPurchaseHistory());
        ArrayList<Order> filteredHistory = new ArrayList<>();

        // Filter based on search inputs
        if ((searchId != null && !searchId.isEmpty()) || (searchDate != null && !searchDate.isEmpty())) {
            for (Order order : purchaseHistory) {
                boolean matchesId = searchId != null && !searchId.isEmpty() &&
                        String.valueOf(order.getId()).contains(searchId);
                boolean matchesDate = searchDate != null && !searchDate.isEmpty() &&
                        order.getPurchaseDate().toString().contains(searchDate);
                if ((matchesId && matchesDate) || (matchesId && searchDate.isEmpty()) || (matchesDate && searchId.isEmpty())) {
                    filteredHistory.add(order);
                }
            }
        } else {
            filteredHistory = purchaseHistory;
        }

        // Set attributes for the JSP page
        request.setAttribute("filteredHistory", filteredHistory);
        request.setAttribute("searchId", searchId);
        request.setAttribute("searchDate", searchDate);

        // Forward the request back to the JSP page
        RequestDispatcher dispatcher = request.getRequestDispatcher("paymentHistory.jsp");
        dispatcher.forward(request, response);
    }
}