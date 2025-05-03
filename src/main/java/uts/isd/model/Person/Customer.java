package uts.isd.model.Person;

import uts.isd.model.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Customer extends User implements Serializable {

    private String phoneNumber;
    private Date dateOfBirth;
    private ArrayList<Order> purchaseHistory = new ArrayList<>();
    private boolean isRegistered;

    // Constructor with minimal info
    public Customer(String firstName, String lastName, String password, String email, UserType type) {
        super(firstName, lastName, password, email, type);
        this.isRegistered = false;
    }

    // Constructor with Address and phone
    public Customer(String firstName, String lastName, String password, String email,
                    Address address, String phoneNumber, UserType type) {
        super(firstName, lastName, password, email, address, type);
        this.phoneNumber = phoneNumber;
        this.isRegistered = false;
    }

    // Constructor with full info including payment
    public Customer(String firstName, String lastName, String password, String email,
                    Address address, String phoneNumber, PaymentInformation paymentInfo, UserType type) {
        super(firstName, lastName, password, email, address, paymentInfo, type);
        this.phoneNumber = phoneNumber;
        this.isRegistered = false;
    }

    // Getters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public ArrayList<Order> getPurchaseHistory() {
        return purchaseHistory;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    // Setters
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }

    // Add to purchase history
    public void addOrder(Order order) {
        this.purchaseHistory.add(order);
    }

    // Convenience method for setting payment info
    public void setPayment(String cardNo, String cvv, String cardHolder) {
        setPaymentInfo(new PaymentInformation(cardNo, cvv, cardHolder, null));
    }
}
