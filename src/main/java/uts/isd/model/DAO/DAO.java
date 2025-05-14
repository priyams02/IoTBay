package uts.isd.model.DAO;

import java.sql.Connection;
import java.sql.SQLException;

public class DAO {
    public static String AnonymousUserEmail;
    private final Connection connection;

    public final CustomerDBManager customerDB;
    public final StaffDBManager   staffDB;
    public final ProductDBManager  productDB;
    public final CartDBManager     cartDB;
    public final OrderDBManager    orderDB;

    public DAO() throws SQLException {
        this.connection = new DBConnector().getConnection();
        this.customerDB = new CustomerDBManager(connection);
        this.staffDB    = new StaffDBManager(connection);
        this.productDB  = new ProductDBManager(connection);
        this.cartDB     = new CartDBManager(connection);
        this.orderDB    = new OrderDBManager(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public CustomerDBManager customers() {
        return customerDB;
    }

    public StaffDBManager staff() {
        return staffDB;
    }

    public ProductDBManager products() {
        return productDB;
    }

    public CartDBManager cart() {
        return cartDB;
    }

    public OrderDBManager orders() {
        return orderDB;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
