package uts.isd.controller.Core;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import uts.isd.model.dao.DBConnector;
import uts.isd.model.dao.DBManager;

import java.io.IOException;
import java.sql.SQLException;

public abstract class IoTWebpageBase extends HttpServlet implements IoTWebpage {

    public static DBConnector connector;
    public static DBManager uDB;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ensureDatabaseSession(request);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ensureDatabaseSession(request);
    }

    private void ensureDatabaseSession(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("UDatabase") == null) {
            try {
                connectToDB();
                session.setAttribute("UDatabase", uDB);
                session.setAttribute("Customers", uDB.getCustomerDAO());
                session.setAttribute("Staff", uDB.getStaffDAO());
                session.setAttribute("Products", uDB.getProductDAO());
            } catch (SQLException e) {
                throw new IOException("Database connection failed: " + e.getMessage());
            }
        }
    }

    public static void connectToDB() throws SQLException {
        try {
            connector = new DBConnector();
            uDB = new DBManager(connector.openConnection());
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver class not found: " + e.getMessage());
        }
    }
}