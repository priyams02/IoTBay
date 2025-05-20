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
@WebServlet(name = "ViewProduct", urlPatterns = {"/ViewProduct"})
public class ViewProductServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String productId = req.getParameter("id");
        if (productId == null || productId.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/ListProducts?err=Missing+ID");
            return;
        }

        // 1) Fetch or create DAO
        HttpSession session = req.getSession();
        DAO dao = (DAO) session.getAttribute("dao");
        if (dao == null) {
            try {
                dao = new DAO();              // your no-arg constructor
                session.setAttribute("dao", dao);
            } catch (SQLException e) {
                throw new ServletException("Database connection error", e);
            }
        }

        try {
            ProductDBManager mgr = dao.products();
            Product p = mgr.findProduct(productId);
            if (p == null) {
                resp.sendRedirect(req.getContextPath() + "/ListProducts?err=Not+found");
                return;
            }

            // 2) Forward to JSP
            req.setAttribute("product", p);
            req.getRequestDispatcher("/ViewProduct.jsp")
                    .forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Error loading product", e);
        }
    }
}

