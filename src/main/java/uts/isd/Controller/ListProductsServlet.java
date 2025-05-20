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

@WebServlet(name = "ListProducts", urlPatterns = "/ListProducts")
public class ListProductsServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("dao") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        DAO dao = (DAO) session.getAttribute("dao");
        ProductDBManager productDB = dao.products();

        try {
            List<Product> products = productDB.listAll();
            session.removeAttribute("searchedProducts"); // ✅ Clear search override
            req.setAttribute("products", products);

            req.getRequestDispatcher("/Listing.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Unable to retrieve product list", e);
        }
    }
}

