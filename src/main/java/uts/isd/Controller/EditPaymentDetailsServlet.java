package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.DAO.ShipmentDBManager;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.Person.User;
import uts.isd.model.Product;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for staff to add new products to IoTBay.
 */
@WebServlet("/EditPaymentDetailsServlet")
public class EditPaymentDetailsServlet extends IoTWebpageBase {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not initialized in session");
        }
        String email =  req.getParameter("Email");
        String cardNo       = req.getParameter("cardNo");
        String CVV          = req.getParameter("CVV");
        String cardHolder   = req.getParameter("cardHolder");
        CustomerDBManager customerDB = dao.customers();
        try {
            Customer current = customerDB.findCustomer(email);
            PaymentInformation newPaymentInformation = new PaymentInformation(cardNo, CVV, cardHolder, null);
            Customer newCustomer = new Customer(
                    current.getFirstName(),
                    current.getLastName(),
                    current.getPassword(),
                    current.getEmail(),
                    current.getAddress(),
                    current.getPhoneNumber(),
                    newPaymentInformation,
                    User.UserType.CUSTOMER
            );
            customerDB.update(current, newCustomer);
            session.setAttribute("cardNo", cardNo);
            session.setAttribute("CVV", CVV);
            session.setAttribute("cardHolder", cardHolder);
            resp.sendRedirect(req.getContextPath() + "/checkout.jsp");

        } catch (SQLException e) {
            throw new ServletException("Update failed", e);

        }

    }
}