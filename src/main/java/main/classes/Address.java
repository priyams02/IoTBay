package main.classes;
public class Address {

    private String number;
    private String streetName;
    private String suburb;
    private String postcode;
    private String city;

    public Address(String number, String streetName, String suburb, String postcode, String city) {
        this.number = number;
        this.streetName = streetName;
        this.suburb = suburb;
        this.postcode = postcode;
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public String getStreetName() {
        return streetName;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return getNumber() + " " + getStreetName() + ", " + getSuburb() + ", " + getPostcode() + ", " + getCity();
    }
}