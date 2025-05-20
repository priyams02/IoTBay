package uts.isd.model.DAO;

import uts.isd.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for Product operations on the PRODUCTS table.
 */
public class ProductDBManager extends AbstractDBManager<Product> {

    public ProductDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public Product add(Product p) throws SQLException {
        String sql = "INSERT INTO PRODUCTS (PRODUCTID, PRODUCTNAME, CATEGORY, PRICE, STOCK) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, clamp(p.getProductId(), 50));
            ps.setString(2, clamp(p.getName(), 100));
            ps.setString(3, clamp(p.getCategory(), 100));
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getStock());
            ps.executeUpdate();
        }
        return p;
    }

    @Override
    public Product get(Product key) throws SQLException {
        return findProduct(key.getProductId());
    }

    /**
     * Find a Product by its productId.
     */
    public Product findProduct(String productId) throws SQLException {
        String sql = "SELECT * FROM PRODUCTS WHERE PRODUCTID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return resultSetToProduct(rs);
                }
            }
        }
        return null;
    }

    /**
     * Find the productId of a product given its name.
     */
    public String findProductID(String name) throws SQLException {
        String sql = "SELECT PRODUCTID FROM PRODUCTS WHERE PRODUCTNAME = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PRODUCTID");
                }
            }
        }
        return null;
    }

    /**
     * Returns the numeric product ID parsed from the model's string field.
     */
    public int findProductID(Product p) {
        try {
            return Integer.parseInt(p.getProductId());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Product.productId is not a valid integer: " + p.getProductId(), ex
            );
        }
    }


    /**
     * Search products by name (case-insensitive substring match).
     */
    public List<Product> search(String byName) throws SQLException {
        String sql = "SELECT * FROM PRODUCTS WHERE UPPER(PRODUCTNAME) LIKE UPPER(?)";
        List<Product> results = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + byName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(resultSetToProduct(rs));
                }
            }
        }
        return results;
    }

    /**
     * List all products.
     */
    public List<Product> listAll() throws SQLException {
        String sql = "SELECT * FROM PRODUCTS";
        List<Product> results = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                results.add(resultSetToProduct(rs));
            }
        }
        return results;
    }

    @Override
    public void update(Product oldP, Product newP) throws SQLException {
        String sql = "UPDATE PRODUCTS SET PRODUCTNAME=?, CATEGORY=?, PRICE=?, STOCK=? WHERE PRODUCTID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, clamp(newP.getName(), 100));
            ps.setString(2, clamp(newP.getCategory(), 100));
            ps.setDouble(3, newP.getPrice());
            ps.setInt(4, newP.getStock());
            ps.setString(5, oldP.getProductId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Product p) throws SQLException {
        String sql = "DELETE FROM PRODUCTS WHERE PRODUCTID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, p.getProductId());
            ps.executeUpdate();
        }
        // Optionally, cascade delete from cart/orderlineitems if needed
    }

    private Product resultSetToProduct(ResultSet rs) throws SQLException {
        String id       = rs.getString("PRODUCTID");
        String name     = rs.getString("PRODUCTNAME");
        String category = rs.getString("CATEGORY");
        double price    = rs.getDouble("PRICE");
        int stock       = rs.getInt("STOCK");
        return new Product(id, name, category, price, stock);
    }
    public List<Product> search(String name, String category) throws SQLException {
        String sql = "SELECT * FROM PRODUCTS WHERE UPPER(PRODUCTNAME) LIKE UPPER(?) AND UPPER(CATEGORY) LIKE UPPER(?)";
        List<Product> results = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + category + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    results.add(resultSetToProduct(rs));
                }
            }
        }
        return results;
    }
    public boolean exists(String productId) throws SQLException {
        String sql = "SELECT 1 FROM PRODUCTS WHERE PRODUCTID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
    private String clamp(String s, int max) {
        if (s == null) return null;
        return s.length() <= max ? s : s.substring(0, max);
    }
}
