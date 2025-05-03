package uts.isd.model;

import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Order implements Serializable {

    private final int id;
    private final Customer owner;
    private final ArrayList<OrderLineItem> products;
    private final float totalCost;
    private String status;
    private final String purchaseDate;
    private final Address address;
    private final PaymentInformation paymentInformation;

    // Constructor
    public Order(int id, Customer owner, ArrayList<OrderLineItem> products, float totalCost,
                 String status, Address address, PaymentInformation paymentInformation) {

        this.id = id;
        this.owner = owner;
        this.products = products;
        this.totalCost = totalCost;
        this.status = status;
        this.address = address;
        this.paymentInformation = paymentInformation;

        // Auto-generate purchase date in standard format
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        this.purchaseDate = dtf.format(LocalDateTime.now());
    }

    // Getters
    public int getId() {
        return id;
    }

    public Customer getOwner() {
        return owner;
    }

    public ArrayList<OrderLineItem> getProducts() {
        return products;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public String getStatus() {
        return status;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getFormattedPurchaseDate() {
        String[] parts = purchaseDate.split("/");

        String year = parts[0];
        Month month = Month.of(Integer.parseInt(parts[1]));
        String dayPart = parts[2].split(" ")[0];

        String ordinal;
        switch (dayPart) {
            case "01": ordinal = "st"; break;
            case "02": ordinal = "nd"; break;
            case "03": ordinal = "rd"; break;
            default:   ordinal = "th"; break;
        }

        return dayPart + ordinal + " of " + month + ", " + year;
    }

    public Address getAddress() {
        return address;
    }

    public PaymentInformation getPaymentInformation() {
        return paymentInformation;
    }

    // Setter
    public void setStatus(String status) {
        this.status = status;
    }
}
