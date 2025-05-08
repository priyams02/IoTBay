package uts.isd.model.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class DAO {
    Connection connection;

    private final CustomerDBManager customerDB;
    private final StaffDBManager   staffDB;
    private final ProductDBManager ProductDB;

    // Later you can add more, like:
    // private final ProductDBManager productDBManager;

    public DAO() throws SQLException {
        this.connection = new DBConnector().getConnection();
        this.customerDB = new CustomerDBManager(connection);
        this.staffDB = new StaffDBManager(connection);
        this.ProductDB = new ProductDBManager(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public ProductDBManager products(){
        return ProductDB;
    }

    public CustomerDBManager customers() {
        return customerDB;
    }
    public StaffDBManager   staff()     {
        return staffDB;
    }

    // Optionally:
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}