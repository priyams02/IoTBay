package uts.isd.controller;

import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.Staff;
import uts.isd.model.Person.User;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CustomerDBManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.model.Product;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/shop")
public class ShopServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        DAO dao = (DAO) getServletContext().getAttribute("dao");
        if (dao == null) {
            throw new ServletException("DAO not found in context");
        }

        // fetch products and role
        List<Product> products;
        try {
            products = dao.products().listAll();
        } catch (SQLException e) {
            throw new ServletException("Failed to load products", e);
        }
        User user = (User) session.getAttribute("loggedInUser");
        boolean isStaff = user instanceof Staff;

        // push into request scope
        req.setAttribute("productList", products);
        req.setAttribute("isStaff", isStaff);

        // forward to JSP
        req.getRequestDispatcher("/shop.jsp").forward(req, resp);
    }
}