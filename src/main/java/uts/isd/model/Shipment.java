package uts.isd.model;

import uts.isd.model.Person.Address;
import java.io.Serializable;

public class Shipment implements Serializable {

    private static int totalShipments = 0;

    private final int shipmentId;
    private Address destination;
    private String status;
    private String shippingOptions;

    public Shipment(Address destination, String status, String shippingOptions) {
        this.shipmentId = totalShipments++;
        this.destination = destination;
        this.status = status;
        this.shippingOptions = shippingOptions;
    }

    // Getters
    public int getShipmentId() {
        return shipmentId;
    }

    public Address getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    public String getShippingOptions() {
        return shippingOptions;
    }

    // Setters
    public void setDestination(Address destination) {
        this.destination = destination;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setShippingOptions(String shippingOptions) {
        this.shippingOptions = shippingOptions;
    }
}
