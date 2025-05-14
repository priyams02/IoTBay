package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ShipmentDBManager;
import uts.isd.model.Shipment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/Shipment/List")
public class ListShipmentsServlet extends IoTWebpageBase {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            DAO dao = new DAO();
            ShipmentDBManager mgr = dao.shipments();
            List<Shipment> list = mgr.listByOrder(orderId);

            req.setAttribute("shipments", list);
            req.setAttribute("orderId", orderId);
            req.getRequestDispatcher("/shipments.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Failed to list shipments", e);
        }
    }
}
