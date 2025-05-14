package uts.isd.model;

import uts.isd.model.Person.Customer;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Invoice implements Serializable {

    private static int totalInvoices = 0;

    private final int invoiceId;
    private final Order order;
    private final Date issuedDate;

    public Invoice(Customer customer, Order order) {
        this.invoiceId = totalInvoices++;
        this.order = order;
        this.issuedDate = Calendar.getInstance().getTime();
    }

    // Getters
    public int getInvoiceId() {
        return invoiceId;
    }

    public Order getOrder() {
        return order;
    }

    public Customer getOwner() {
        return order.getOwner();
    }

    public Date getIssuedDate() {
        return issuedDate;
    }
}
