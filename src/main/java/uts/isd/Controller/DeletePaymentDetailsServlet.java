package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.DAO.DAO;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.Person.User;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for staff to add new products to IoTBay.
 */
@WebServlet("/DeletePaymentDetailsServlet")
public class DeletePaymentDetailsServlet extends IoTWebpageBase {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        String email =  req.getParameter("Email");
        CustomerDBManager customerDB = dao.customers();
        try {
            Customer current = customerDB.findCustomer(email);
            Customer newCustomer = new Customer(
                    current.getFirstName(),
                    current.getLastName(),
                    current.getPassword(),
                    current.getEmail(),
                    current.getAddress(),
                    current.getPhoneNumber(),
                    null,
                    User.UserType.CUSTOMER
            );
            customerDB.update(current, newCustomer);
            session.setAttribute("cardNo", null);
            session.setAttribute("CVV", null);
            session.setAttribute("cardHolder", null);
            resp.sendRedirect(req.getContextPath() + "/checkout.jsp");

        } catch (SQLException e) {
            throw new ServletException("Delete failed", e);

        }

    }
}