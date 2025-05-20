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

@WebServlet("/ListProducts")
public class ListProductsServlet extends IoTWebpageBase {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // 1. Instantiate a fresh DAO (no session required)
            DAO dao = new DAO();
            ProductDBManager productDB = dao.products();

            // 2. Read optional 'search' parameter
            String search = req.getParameter("search");

            // 3. Fetch either all products or the filtered list
            List<Product> products;
            if (search != null && !search.isEmpty()) {
                products = productDB.search(search);
            } else {
                products = productDB.listAll();
            }

            // 4. Make them available to the JSP
            req.setAttribute("products", products);
            req.setAttribute("searchParam", search);

            // 5. Forward to your Listing.jsp
            req.getRequestDispatcher("/Listing.jsp")
                    .forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException("Failed to list products", e);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If you ever post to this URL, delegate back to GET
        doGet(req, resp);
    }
}
