package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.Person.Address;
import uts.isd.model.Shipment;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Shipment/Create")
public class CreateShipmentServlet extends IoTWebpageBase {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/shipmentForm.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String ctx = req.getContextPath();
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            Address dest = new Address(
                    req.getParameter("streetNumber"),
                    req.getParameter("streetName"),
                    req.getParameter("suburb"),
                    req.getParameter("postcode"),
                    req.getParameter("city")
            );
            String status = req.getParameter("status");
            String opts   = req.getParameter("shippingOptions");

            DAO dao = new DAO();
            // use the new convenience method:
            dao.addShipment(new Shipment(orderId, dest, status, opts));

            resp.sendRedirect(ctx + "/Shipment/List?orderId=" + orderId);
        } catch (NumberFormatException | SQLException e) {
            throw new ServletException("Failed to create shipment", e);
        }
    }
}
