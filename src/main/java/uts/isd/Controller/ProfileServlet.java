package uts.isd.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import uts.isd.Controller.Core.IoTWebpageBase;
import uts.isd.model.DAO.DAO;
import uts.isd.model.DAO.CustomerDBManager;
import uts.isd.model.DAO.StaffDBManager;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.Staff;
import uts.isd.model.Person.User;

import java.io.IOException;


@WebServlet("/Profile")
public class ProfileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            User user = (User) session.getAttribute("loggedInUser");
            req.setAttribute("loggedInUser", user);
            req.getRequestDispatcher("/Profile.jsp")
                    .forward(req, resp);
        } else {
            // not logged in → back to login
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }
}
