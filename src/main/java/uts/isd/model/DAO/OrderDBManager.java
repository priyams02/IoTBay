package uts.isd.model.DAO;

import uts.isd.model.Order;
import uts.isd.model.OrderLineItem;
import uts.isd.model.Person.Address;
import uts.isd.model.Person.PaymentInformation;
import uts.isd.model.Person.Customer;
import uts.isd.model.Product;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for managing Orders.
 */
public class OrderDBManager extends AbstractDBManager<Order> {
    public OrderDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    public Order makeOrder(
            String owner,
            List<OrderLineItem> cartItems,
            Address address,
            PaymentInformation pi
    ) throws SQLException {
        float totalCost = 0f;
        StringBuilder pids = new StringBuilder();
        StringBuilder quas = new StringBuilder();

        ProductDBManager prodDao = new ProductDBManager(connection);
        for (OrderLineItem oli : cartItems) {
            Product p = oli.getProduct();
            String prodId = p.getProductId();
            int quantity = oli.getQuantity();
            pids.append(prodId).append(":");
            quas.append(quantity).append(":");
            totalCost += (float) oli.getTotalCost();

            int newStock = p.getStock() - quantity;
            Product updated = new Product(
                    prodId,
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    newStock
            );
            prodDao.update(p, updated);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String purchaseDate = dtf.format(LocalDateTime.now());

        String sql = "INSERT INTO ORDERS " +
                "(OWNER, PRICE, STATUS, PRODUCTS, QUANTITY, PURCHASEDATE, " +
                "STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY, CARDNUMBER, CVV, CARDHOLDER) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, owner.toLowerCase());
            ps.setFloat(2, totalCost);
            ps.setString(3, "Confirmed");
            ps.setString(4, pids.toString());
            ps.setString(5, quas.toString());
            ps.setString(6, purchaseDate);
            ps.setString(7, clamp(address.getNumber(), 10));
            ps.setString(8, clamp(address.getStreetName(), 100));
            ps.setString(9, clamp(address.getSuburb(), 100));
            ps.setString(10, clamp(address.getPostcode(), 10));
            ps.setString(11, clamp(address.getCity(), 100));
            ps.setString(12, clamp(pi.getCardNo(), 25));
            ps.setString(13, clamp(pi.getCVV(), 5));
            ps.setString(14, clamp(pi.getCardHolder(), 200));

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    new CartDBManager(connection).clear(owner);
                    return findOrder(orderId, owner);
                }
            }
        }
        return null;
    }

    public Order findOrder(int orderId, String owner) throws SQLException {
        String sql = "SELECT * FROM ORDERS WHERE ORDERID = ? AND OWNER = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setString(2, owner.toLowerCase());
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) {
                    return resultSetToOrder(r);
                }
            }
        }
        return null;
    }

    public List<Order> listByOwner(String owner) throws SQLException {
        String sql = "SELECT * FROM ORDERS WHERE OWNER = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toLowerCase());
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    orders.add(resultSetToOrder(r));
                }
            }
        }
        return orders;
    }

    // NEW: Search orders by date
    public List<Order> findOrdersByDate(String owner, String date) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM ORDERS WHERE OWNER = ? AND DATE(PURCHASEDATE) = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, owner.toLowerCase());
            ps.setString(2, date); // Format must be YYYY-MM-DD
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(resultSetToOrder(rs));
                }
            }
        }
        return orders;
    }



    public void cancelOrder(int orderId, String owner) throws SQLException {
        Order order = findOrder(orderId, owner);
        if (order == null) return;

        ProductDBManager prodDao = new ProductDBManager(connection);
        for (OrderLineItem oli : order.getProducts()) {
            Product p = oli.getProduct();
            int quantity = oli.getQuantity();
            int newStock = p.getStock() + quantity;
            Product updated = new Product(
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    newStock
            );
            prodDao.update(p, updated);
        }

        String sql = "UPDATE ORDERS SET STATUS = ? WHERE ORDERID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "Cancelled");
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    @Override public Order add(Order o) throws SQLException {
        throw new UnsupportedOperationException("Use makeOrder instead");
    }
    @Override public Order get(Order o) {
        throw new UnsupportedOperationException();
    }
    @Override public void update(Order oldO, Order newO) {
        throw new UnsupportedOperationException();
    }
    @Override public void delete(Order o) throws SQLException {
        throw new UnsupportedOperationException("Physical delete not supported");
    }

    // --- FIXED: Correctly reconstruct order items from PRODUCTS/QUANTITY columns ---
    private Order resultSetToOrder(ResultSet r) throws SQLException {
        int id = r.getInt("ORDERID");
        Customer owner = new CustomerDBManager(connection)
                .findCustomer(r.getString("OWNER"));
        float price = r.getFloat("PRICE");
        String status = r.getString("STATUS");
        String purchaseDate = r.getString("PURCHASEDATE");
        Address addr = new Address(
                r.getString("STREETNUMBER"),
                r.getString("STREETNAME"),
                r.getString("SUBURB"),
                r.getString("POSTCODE"),
                r.getString("CITY")
        );
        PaymentInformation pi = new PaymentInformation(
                r.getString("CARDNUMBER"),
                r.getString("CVV"),
                r.getString("CARDHOLDER"),
                null
        );
        // Correct: Parse from order row, not from cart!
        List<OrderLineItem> items = parseOrderLineItems(
                r.getString("PRODUCTS"),
                r.getString("QUANTITY")
        );
        return new Order(id, owner, new ArrayList<>(items), price, status, addr, pi);
    }

    // Helper to reconstruct line items from PRODUCTS and QUANTITY fields
    private List<OrderLineItem> parseOrderLineItems(String productsField, String quantityField) throws SQLException {
        List<OrderLineItem> items = new ArrayList<>();
        if (productsField == null || quantityField == null) return items;
        String[] pids = productsField.split(":");
        String[] quants = quantityField.split(":");
        ProductDBManager prodDao = new ProductDBManager(connection);
        for (int i = 0; i < pids.length && i < quants.length; i++) {
            if (!pids[i].isEmpty() && !quants[i].isEmpty()) {
                Product prod = prodDao.findProduct(pids[i]);
                int quantity = Integer.parseInt(quants[i]);
                items.add(new OrderLineItem(prod, null, quantity));
            }
        }
        return items;
    }

    private String clamp(String s, int max) {
        return (s == null) ? null : (s.length() <= max ? s : s.substring(0, max));
    }
}