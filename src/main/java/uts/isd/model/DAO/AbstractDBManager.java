package uts.isd.model.DAO;

import uts.isd.model.Person.*;
import uts.isd.model.Order;
import uts.isd.model.OrderLineItem;
import uts.isd.model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class AbstractDBManager<T> {
    protected final Statement statement;
    protected final Connection connection;
    /**
     * Used when a Customer is Anonymous and not registered with IoTBay.
     * <br><br>
     * Use this for consistency when handling sensitive information and
     * differentiating between Registered and Unregistered Customers.
     */
    public static final String AnonymousUserEmail = "ANONYMOUS@USER.UTS.EDU.AU";
    public ArrayList<Customer> customers;
    public ArrayList<Staff> staff;
    public ArrayList<Product> products;
    public ArrayList<OrderLineItem> cart;
    public ArrayList<Order> orders;

    public AbstractDBManager(Connection connection) throws SQLException {
        this.connection = connection;
        statement = connection.createStatement();
    }

    public abstract T add(T object) throws SQLException;
    public abstract T get(T object) throws SQLException;
    public abstract void update(T oldObject, T newObject) throws SQLException;
    public abstract void delete(T object) throws SQLException;


}


