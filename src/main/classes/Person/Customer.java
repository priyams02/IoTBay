package classes.person;

import java.io.serializable;
import java.util.ArrayList;
import java.util.Date;

public class Customer extends User implements Serializable{
    private String phoneNumber;
    private Date DOB;
//    Possible future parameters or edited
//    private ArrayList<Order> purchaseHistory;
//    private PaymentInformation payment;
    private boolean isRegistered;

    public Customer(String firstName, String lastName, String password, String email) {
        super(firstName, lastName, password, email, UserType.Customer);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Date getDOB() {
        return DOB;
    }
    public boolean getRegistration() {
        return bIsRegistered;
    }
    public void setPhoneNumber(String x) {
        phoneNumber = x;
    }

    public void setRegistered() {
        IsRegistered = true;
    }
}
