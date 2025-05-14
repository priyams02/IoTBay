package uts.isd.model;

import uts.isd.model.Person.Address;
import java.io.Serializable;

public class Shipment implements Serializable {
    private final int shipmentId;   // the DB PK
    private final int orderId;      // FK back to ORDERS
    private Address destination;
    private String status;
    private String shippingOptions;

    /**
     * Constructor for an existing shipment loaded from the DB.
     * @param shipmentId the DB‐assigned ID
     * @param orderId    the order this shipment belongs to
     */
    public Shipment(int shipmentId, int orderId,
                    Address destination, String status, String shippingOptions) {
        this.shipmentId     = shipmentId;
        this.orderId        = orderId;
        this.destination    = destination;
        this.status         = status;
        this.shippingOptions = shippingOptions;
    }

    /**
     * Constructor for a new shipment (ID will be assigned by the DB on insert).
     * @param orderId the order this shipment belongs to
     */
    public Shipment(int orderId,
                    Address destination, String status, String shippingOptions) {
        this(0, orderId, destination, status, shippingOptions);
    }

    // Getters
    public int getShipmentId()    { return shipmentId; }
    public int getOrderId()       { return orderId; }
    public Address getDestination()   { return destination; }
    public String getStatus()        { return status; }
    public String getShippingOptions(){ return shippingOptions; }

    // Setters for editable fields
    public void setDestination(Address destination)         { this.destination = destination; }
    public void setStatus(String status)                   { this.status = status; }
    public void setShippingOptions(String shippingOptions) { this.shippingOptions = shippingOptions; }
}
