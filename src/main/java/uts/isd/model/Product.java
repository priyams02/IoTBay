package uts.isd.model;

import java.io.Serializable;
import java.util.UUID;

public class Product implements Serializable {
    private String productId;
    private String name;
    private String category;
    private double price;
    private int stock;

    public Product() {
    }

    public Product(String productId, String name, String category, double price, int stock) {
        this.productId = productId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    /**
     * Convenience constructor for when you only have name/desc, price & qty.
     * Generates a random UUID for the productId.
     */
    public Product(String name, String category, double price, int stock) {
        this(UUID.randomUUID().toString(), name, category, price, stock);
    }

    // Getters
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // Setters
    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}