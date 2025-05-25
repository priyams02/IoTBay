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

@WebServlet("/Shipment/Update")
public class UpdateShipmentServlet extends IoTWebpageBase {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            DAO dao = new DAO();
            Shipment s = dao.findShipment(id);
            req.setAttribute("shipment", s);
            req.getRequestDispatcher("/shipmentForm.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Failed to load shipment", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String ctx = req.getContextPath();

        String idRaw        = req.getParameter("id");
        String orderIdRaw   = req.getParameter("orderId");
        String streetNumber = req.getParameter("streetNumber");
        String streetName   = req.getParameter("streetName");
        String suburb       = req.getParameter("suburb");
        String postcode     = req.getParameter("postcode");
        String city         = req.getParameter("city");
        String status       = req.getParameter("status");
        String shippingOpts = req.getParameter("shippingOptions");

        int id, orderId;
        try {
            id      = Integer.parseInt(idRaw);
            orderId = Integer.parseInt(orderIdRaw);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid shipment or order ID.");
            req.getRequestDispatcher("/shipmentForm.jsp").forward(req, resp);
            return;
        }

        String err = validateShipment(streetNumber, streetName, suburb, postcode, city, status, shippingOpts);
        if (err != null) {
            req.setAttribute("error", err);
            req.getRequestDispatcher("/shipmentForm.jsp").forward(req, resp);
            return;
        }

        Address dest = new Address(streetNumber, streetName, suburb, postcode, city);
        try {
            DAO dao = new DAO();
            Shipment old = dao.findShipment(id);
            Shipment updated = new Shipment(orderId, dest, status, shippingOpts);
            dao.updateShipment(old, updated);
            resp.sendRedirect(ctx + "/Shipment/List?orderId=" + orderId);
        } catch (SQLException e) {
            throw new ServletException("Failed to update shipment", e);
        }
    }

    private String validateShipment(
            String streetNumber,
            String streetName,
            String suburb,
            String postcode,
            String city,
            String status,
            String opts
    ) {
        if (streetNumber == null || !streetNumber.matches("\\d+")) {
            return "Street number must be digits only.";
        }
        if (streetName == null || streetName.trim().isEmpty()
                || streetName.matches(".*\\d.*")) {
            return "Street name cannot contain numbers.";
        }
        if (suburb == null || suburb.trim().isEmpty()
                || suburb.matches(".*\\d.*")) {
            return "Suburb cannot contain numbers.";
        }
        if (postcode == null || !postcode.matches("\\d{4,5}")) {
            return "Postcode must be 4–5 digits.";
        }
        if (city == null || city.trim().isEmpty()
                || city.matches(".*\\d.*")) {
            return "City cannot contain numbers.";
        }
        if (status == null || status.trim().isEmpty()) {
            return "Status cannot be empty.";
        }
        if (opts == null || opts.trim().isEmpty()) {
            return "Shipping options cannot be empty.";
        }
        return null;
    }
}
