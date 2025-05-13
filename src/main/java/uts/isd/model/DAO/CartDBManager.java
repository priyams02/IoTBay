package uts.isd.model.DAO;

import uts.isd.model.OrderLineItem;
import uts.isd.model.Person.Customer;
import uts.isd.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for managing shopping cart (order line items).
 */
public class CartDBManager extends AbstractDBManager<OrderLineItem> {
    public CartDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    /**
     * Adds an item to the cart: if the product already exists, increment quantity (up to stock).
     */
    @Override
    public OrderLineItem add(OrderLineItem oli) throws SQLException {
        Product p = oli.getProduct();
        // Get numeric ID by parsing the product's string ID
        int pid = new ProductDBManager(connection).findProductID(p);
        String owner = oli.getOwner() != null
                ? oli.getOwner().getEmail().toLowerCase()
                : AnonymousUserEmail;

        // If already present, bump quantity up to stock and return
        if (preventDuplicates(pid, owner)) {
            return oli;
        }

        // Insert new line
        String sql = "INSERT INTO ORDERLINEITEM (OWNER, PRODUCTID, QUANTITY) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner);
            ps.setInt(2, pid);
            ps.setInt(3, oli.getQuantity());
            ps.executeUpdate();
        }
        return oli;
    }

    /**
     * Lists all items in a customer's cart.
     */
    public List<OrderLineItem> listCart(String owner) throws SQLException {
        String sql = "SELECT * FROM ORDERLINEITEM WHERE OWNER = ?";
        List<OrderLineItem> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toLowerCase());
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    list.add(resultSetToOLI(r));
                }
            }
        }
        return list;
    }

    /**
     * Updates the quantity for a given product in the cart.
     */
    public void updateQuantity(int pid, String owner, int newQuantity) throws SQLException {
        String sql = "UPDATE ORDERLINEITEM SET QUANTITY = ? WHERE PRODUCTID = ? AND OWNER = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, pid);
            ps.setString(3, owner.toLowerCase());
            ps.executeUpdate();
        }
    }

    /**
     * Removes a single line item from the cart.
     */
    @Override
    public void delete(OrderLineItem oli) throws SQLException {
        Product p = oli.getProduct();
        int pid = new ProductDBManager(connection).findProductID(p);
        String owner = oli.getOwner().getEmail().toLowerCase();

        String sql = "DELETE FROM ORDERLINEITEM WHERE PRODUCTID = ? AND OWNER = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.setString(2, owner);
            ps.executeUpdate();
        }
    }

    /**
     * Clears the entire cart for a given owner.
     */
    public void clear(String owner) throws SQLException {
        String sql = "DELETE FROM ORDERLINEITEM WHERE OWNER = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toLowerCase());
            ps.executeUpdate();
        }
    }

    @Override
    public OrderLineItem get(OrderLineItem key) {
        throw new UnsupportedOperationException("Use listCart() to fetch items");
    }

    @Override
    public void update(OrderLineItem oldObject, OrderLineItem newObject) {
        throw new UnsupportedOperationException("Use updateQuantity() to change quantity");
    }

    // -- Helper methods from original DBManager --

    /**
     * Checks for an existing line and increments its quantity if found.
     */
    private boolean preventDuplicates(int pid, String owner) throws SQLException {
        String sqlCheck = "SELECT QUANTITY FROM ORDERLINEITEM WHERE PRODUCTID = ? AND OWNER = ?";
        try (PreparedStatement ps = connection.prepareStatement(sqlCheck)) {
            ps.setInt(1, pid);
            ps.setString(2, owner.toLowerCase());
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) {
                    int current = r.getInt("QUANTITY");
                    int stock = new ProductDBManager(connection)
                            .findProduct(String.valueOf(pid))
                            .getStock();
                    int updated = Math.min(current + 1, stock);
                    updateQuantity(pid, owner, updated);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Maps a result row to an OrderLineItem.
     */
    private OrderLineItem resultSetToOLI(ResultSet r) throws SQLException {
        int pid = r.getInt("PRODUCTID");
        int qty = r.getInt("QUANTITY");
        String ownerEmail = r.getString("OWNER");
        Customer owner = new CustomerDBManager(connection).findCustomer(ownerEmail);
        Product product = new ProductDBManager(connection).findProduct(String.valueOf(pid));
        return new OrderLineItem(product, owner, qty);
    }
}
