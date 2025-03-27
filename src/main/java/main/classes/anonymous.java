package main.classes;

public class Anonymous implements Serializable {
    private int id;
    private String phoneNumber;
    // private Address address
    // private PaymentInformation payment

    public anonymous() {}

    public anonymous(int id, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}