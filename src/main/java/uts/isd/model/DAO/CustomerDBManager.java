package uts.isd.model.DAO;

import uts.isd.model.Person.Address;
import uts.isd.model.Person.Customer;
import uts.isd.model.Person.PaymentInformation;

import java.sql.*;

public class CustomerDBManager extends AbstractDBManager<Customer> {

    public CustomerDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public Customer add(Customer c) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS " +
                "(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, STREETNUMBER, STREETNAME, " +
                "SUBURB, POSTCODE, CITY, PHONENUMBER, CARDNUMBER, CVV, CARDHOLDER, EXPIRYDATE) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Address a = c.getAddress();
            PaymentInformation p = c.getPaymentInfo();

            ps.setString(1, clamp(c.getFirstName(), 100));
            ps.setString(2, clamp(c.getLastName(), 100));
            ps.setString(3, clamp(c.getPassword(), 100));
            ps.setString(4, clamp(c.getEmail().toLowerCase(), 100));
            ps.setString(5, clamp(a.getNumber(), 10));
            ps.setString(6, clamp(a.getStreetName(), 100));
            ps.setString(7, clamp(a.getSuburb(), 100));
            ps.setString(8, clamp(a.getPostcode(), 10));
            ps.setString(9, clamp(a.getCity(), 100));
            ps.setString(10, clamp(c.getPhoneNumber(), 15));
            ps.setString(11, clamp(p.getCardNo(), 25));
            ps.setString(12, clamp(p.getCVV(), 5));
            ps.setString(13, clamp(p.getCardHolder(), 200));
            ps.setString(14, p.getExpiryDate() != null
                    ? p.getExpiryDate().toString()
                    : null);

            ps.executeUpdate();
        }

        return c;
    }

    @Override
    public Customer get(Customer c) throws SQLException {
        return findCustomer(c.getEmail());
    }

    public Customer findCustomer(String email) throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS WHERE EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) {
                    return resultSetToCustomer(r);
                }
            }
        }
        return null;
    }

    @Override
    public void update(Customer oldCustomer, Customer newCustomer) throws SQLException {
        String sql = "UPDATE CUSTOMERS SET " +
                "FIRSTNAME=?, LASTNAME=?, PASSWORD=?, EMAIL=?, PHONENUMBER=?, " +
                "STREETNUMBER=?, STREETNAME=?, SUBURB=?, POSTCODE=?, CITY=?, " +
                "CARDNUMBER=?, CVV=?, CARDHOLDER=?, EXPIRYDATE=? " +
                "WHERE EMAIL=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Address a = newCustomer.getAddress();
            PaymentInformation p = newCustomer.getPaymentInfo();

            ps.setString(1, clamp(newCustomer.getFirstName(), 100));
            ps.setString(2, clamp(newCustomer.getLastName(), 100));
            ps.setString(3, clamp(newCustomer.getPassword(), 100));
            ps.setString(4, clamp(newCustomer.getEmail().toLowerCase(), 100));
            ps.setString(5, clamp(newCustomer.getPhoneNumber(), 15));
            ps.setString(6, clamp(a.getNumber(), 10));
            ps.setString(7, clamp(a.getStreetName(), 100));
            ps.setString(8, clamp(a.getSuburb(), 100));
            ps.setString(9, clamp(a.getPostcode(), 10));
            ps.setString(10, clamp(a.getCity(), 100));
            ps.setString(11, clamp(p.getCardNo(), 25));
            ps.setString(12, clamp(p.getCVV(), 5));
            ps.setString(13, clamp(p.getCardHolder(), 200));
            ps.setString(14, p.getExpiryDate() != null
                    ? p.getExpiryDate().toString()
                    : null);
            ps.setString(15, oldCustomer.getEmail().toLowerCase());

            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Customer c) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, c.getEmail().toLowerCase());
            ps.executeUpdate();
        }
    }

    private Customer resultSetToCustomer(ResultSet r) throws SQLException {
        String email     = r.getString("EMAIL");
        String firstName = r.getString("FIRSTNAME");
        String lastName  = r.getString("LASTNAME");
        String password  = r.getString("PASSWORD");
        String phone     = r.getString("PHONENUMBER");

        Address a = new Address(
                r.getString("STREETNUMBER"),
                r.getString("STREETNAME"),
                r.getString("SUBURB"),
                r.getString("POSTCODE"),
                r.getString("CITY")
        );

        PaymentInformation p = new PaymentInformation(
                r.getString("CARDNUMBER"),
                r.getString("CVV"),
                r.getString("CARDHOLDER"),
                r.getString("EXPIRYDATE")  // stored as TEXT in SQLite
        );

        Customer c = new Customer(
                firstName, lastName, password, email, a, phone, Customer.UserType.CUSTOMER
        );
        c.setPaymentInfo(p);
        return c;
    }

    private String clamp(String str, int maxLength) {
        if (str == null) return "";
        return (str.length() <= maxLength) ? str : str.substring(0, maxLength);
    }
}
