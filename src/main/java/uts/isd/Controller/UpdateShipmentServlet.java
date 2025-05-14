package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ShipmentDBManager;
import uts.isd.model.Person.Address;
import uts.isd.model.Shipment;

import java.io.IOException;

@WebServlet("/Shipment/Update")
public class UpdateShipmentServlet extends IoTWebpageBase {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            DAO dao = new DAO();
            ShipmentDBManager mgr = dao.shipments();
            Shipment s = mgr.findById(id);
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
        try {
            int id      = Integer.parseInt(req.getParameter("id"));
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
            ShipmentDBManager mgr = dao.shipments();
            Shipment old = mgr.findById(id);
            Shipment updated = new Shipment(orderId, dest, status, opts);
            mgr.update(old, updated);

            resp.sendRedirect(ctx + "/Shipment/List?orderId=" + orderId);
        } catch (Exception e) {
            throw new ServletException("Failed to update shipment", e);
        }
    }
}
