package uts.isd.model.Person;

import java.io.Serializable;

public class User implements Serializable {
    private static int totalUsers = 0;

    public final int ID;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private UserType type;
    private Address address;
    private PaymentInformation paymentInfo;

    // Full constructor with all fields
    public User(String firstName, String lastName, String password, String email, Address address, PaymentInformation paymentInfo, UserType type) {
        this.ID = totalUsers++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.paymentInfo = paymentInfo;
        this.type = type;
    }

    // Constructor without PaymentInformation
    public User(String firstName, String lastName, String password, String email, Address address, UserType type) {
        this(firstName, lastName, password, email, address, null, type);
    }

    // Constructor without Address and PaymentInformation
    public User(String firstName, String lastName, String password, String email, UserType type) {
        this(firstName, lastName, password, email, null, null, type);
    }

    // Default constructor
    public User() {
        this.ID = totalUsers++;
    }

    // Getters
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public UserType getType() { return type; }
    public Address getAddress() { return address; }
    public PaymentInformation getPaymentInfo() { return paymentInfo; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setAddress(Address address) { this.address = address; }
    public void setPaymentInfo(PaymentInformation paymentInfo) { this.paymentInfo = paymentInfo; }
    public void setType(UserType type) { this.type = type; }

    public enum UserType {
        CUSTOMER,
        STAFF
    }
}