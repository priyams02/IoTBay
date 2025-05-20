package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.ProductDBManager;
import uts.isd.model.Product;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name="Shop", urlPatterns="/shop")
public class ShopServlet extends IoTWebpageBase {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Create DAO & get product manager
            DAO dao = new DAO();
            ProductDBManager mgr = dao.products();

            // Fetch all products
            List<Product> products = mgr.listAll();

            // Put into request
            req.setAttribute("products", products);

            // Forward to JSP
            req.getRequestDispatcher("/shop.jsp")
                    .forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Unable to load products for shop", e);
        }
    }
}
