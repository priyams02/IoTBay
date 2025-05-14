package uts.isd.model;

import uts.isd.model.Person.Customer;
import java.io.Serializable;

public class OrderLineItem implements Serializable {

    private final Product product;
    private final Customer owner;
    private int quantity;
    private double totalCost;  // changed to double

    public OrderLineItem(Product product, Customer owner, int quantity) {
        this.product = product;
        this.owner = owner;
        this.quantity = quantity;

        if (product != null) {
            this.totalCost = product.getPrice() * quantity;
        } else {
            System.out.println("Warning: Product is null");
            this.totalCost = 0.0;
        }
    }

    public Product getProduct() {
        return product;
    }

    public Customer getOwner() {
        return owner;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        updateTotalCost();
    }

    private void updateTotalCost() {
        if (product != null) {
            totalCost = product.getPrice() * quantity;
        }
    }
}
