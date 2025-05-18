package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ShipmentDBManager;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/Shipment/Delete")
public class DeleteShipmentServlet extends IoTWebpageBase {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String ctx = req.getContextPath();
        try {
            int id      = Integer.parseInt(req.getParameter("id"));
            int orderId = Integer.parseInt(req.getParameter("orderId"));

            DAO dao = new DAO();
            ShipmentDBManager mgr = dao.shipments();
            mgr.deleteById(id);

            resp.sendRedirect(ctx + "/Shipment/List?orderId=" + orderId);
        } catch (SQLException e) {
            throw new ServletException("Failed to delete shipment", e);
        }
    }
}
