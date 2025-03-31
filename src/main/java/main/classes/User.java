package main.classes;

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

    // Parameterized constructor
    public User(String firstName, String lastName, String password, String email,Address address,UserType type) {
        this.ID = totalUsers++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.type = type;
    }

    // Default constructor
    public User() {
        this.ID = totalUsers++;
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPassword() {
        return password;
    }
    public String getEmail() {
        return email;
    }
    public UserType getType() {
        return type;
    }
    public Address getAddress() { return address; }

    // New setters for registration
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String LastName) {this.lastName = lastName;}
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public enum UserType {
        CUSTOMER,
        STAFF
    }
}