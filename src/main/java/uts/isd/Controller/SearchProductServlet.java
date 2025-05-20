package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "SearchProduct", urlPatterns = "/SearchProduct")
public class SearchProductServlet extends IoTWebpageBase {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String params = trim(req.getParameter("Params"));
        if (params.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/ListProducts?err="
                    + URLEncoder.encode("Search term required", StandardCharsets.UTF_8));
            return;
        }
        String encoded = URLEncoder.encode(params, StandardCharsets.UTF_8);
        // redirect through ListProductsServlet with search param
        resp.sendRedirect(req.getContextPath() + "/ListProducts?search=" + encoded);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // always redirect to the listing servlet
        resp.sendRedirect(req.getContextPath() + "/ListProducts");
    }

    private String trim(String s) {
        return s == null ? "" : s.trim();
    }
}
