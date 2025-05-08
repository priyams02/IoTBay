package uts.isd.model.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class DAO {
    Connection connection;

    private final CustomerDBManager customerDBManager;
    // Later you can add more, like:
    // private final ProductDBManager productDBManager;

    public DAO() throws SQLException {
        this.connection = new DBConnector().getConnection();
        this.customerDBManager = new CustomerDBManager(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public CustomerDBManager customers() {
        return customerDBManager;
    }

    // Optionally:
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}