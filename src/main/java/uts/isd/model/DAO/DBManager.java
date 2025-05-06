package uts.isd.model.DAO;

import uts.isd.model.Person.*;
import uts.isd.model.Order;
import uts.isd.model.OrderLineItem;
import uts.isd.model.Invoice;
import uts.isd.model.Shipment;
import uts.isd.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public abstract class DBManager<T> {
    protected final Statement statement;
    protected final Connection connection;
    /**
     * Used when a Customer is Anonymous and not registered with IoTBay.
     * <br><br>
     * Use this for consistency when handling sensitive information and
     * differentiating between Registered and Unregistered Customers.
     */
    public static final String AnonymousUserEmail = "ANONYMOUS@USER.UTS";


    private final Connection connection;

    public ArrayList<Customer> customers;
    public ArrayList<Staff> staff;
    public ArrayList<Product> products;
    public ArrayList<OrderLineItem> cart;
    public ArrayList<Order> orders;

    public DBManager(Connection connection) throws SQLException {
        this.connection = connection;
        try (Statement pragma = connection.createStatement()) {
            pragma.execute("PRAGMA foreign_keys = ON;");
        }

    }

    /*
    This is where you add the methods for CRUD in our DB
    I'll add an example shortly
     */

    /**
     * Finds a Customer with an Email.
     *
     * @param email The Email address to search.
     * @return Customer with Email email.
     * @throws SQLException
     */

    public Customer findCustomer(String email) throws SQLException {
        email = email.toLowerCase();

        if (email.equals(AnonymousUserEmail.toLowerCase())) {
            return null;
        }

        String sql = "SELECT * FROM CUSTOMERS WHERE EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToCustomer(rs);
                }
            }
        }

        return null;
    }

    protected abstract T add(T object) throws SQLException;
    protected abstract T get(T object) throws SQLException;
    protected abstract void update(T oldObject, T newObject) throws SQLException;
    protected abstract void delete(T object) throws SQLException;


}


