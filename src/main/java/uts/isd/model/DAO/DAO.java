package uts.isd.model.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import uts.isd.model.Shipment;

public class DAO {
    public static String AnonymousUserEmail;

    private final Connection connection;
    public final CustomerDBManager customerDB;
    public final StaffDBManager   staffDB;
    public final ProductDBManager  productDB;
    public final CartDBManager     cartDB;
    public final OrderDBManager    orderDB;
    public final ShipmentDBManager shipmentDB;
    public final AccessLogDBManager AccessLogDB;

    public DAO() throws SQLException {
        this.connection = new DBConnector().getConnection();
        this.customerDB = new CustomerDBManager(connection);
        this.staffDB    = new StaffDBManager(connection);
        this.productDB  = new ProductDBManager(connection);
        this.cartDB     = new CartDBManager(connection);
        this.orderDB    = new OrderDBManager(connection);
        this.shipmentDB = new ShipmentDBManager(connection);
        this.AccessLogDB = new AccessLogDBManager(connection);
    }

    public Connection getConnection() {
        return connection;
    }

    public CustomerDBManager customers() {
        return customerDB;
    }

    public StaffDBManager staff() {
        return staffDB;
    }

    public ProductDBManager products() {
        return productDB;
    }

    public CartDBManager cart() {
        return cartDB;
    }

    public OrderDBManager orders() {
        return orderDB;
    }

    public ShipmentDBManager shipments() {
        return shipmentDB;
    }

    public AccessLogDBManager AccessLogs() {return AccessLogDB;}
    }

    // ─── Shipment Methods ───────────────────────────────────────────

    /** Create a new Shipment and return it (with its DB-generated ID). */
    public Shipment addShipment(Shipment s) throws SQLException {
        return shipmentDB.add(s);
    }

    /** Find a single Shipment by its primary key. */
    public Shipment findShipment(int shipmentId) throws SQLException {
        return shipmentDB.findById(shipmentId);
    }

    /** List all Shipments for a given orderId. */
    public List<Shipment> listShipmentsByOrder(int orderId) throws SQLException {
        return shipmentDB.listByOrder(orderId);
    }

    /** Update an existing Shipment record. */
    public void updateShipment(Shipment oldS, Shipment newS) throws SQLException {
        shipmentDB.update(oldS, newS);
    }

    /** Delete a Shipment by its primary key. */
    public void deleteShipment(int shipmentId) throws SQLException {
        shipmentDB.deleteById(shipmentId);
    }


    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
