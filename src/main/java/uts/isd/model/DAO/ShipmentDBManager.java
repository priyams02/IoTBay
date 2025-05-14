package uts.isd.model.DAO;

import uts.isd.model.Shipment;
import uts.isd.model.Person.Address;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDBManager extends AbstractDBManager<Shipment> {
    public ShipmentDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    /** Create a new shipment record */
    @Override
    public Shipment add(Shipment s) throws SQLException {
        String sql = "INSERT INTO SHIPMENTS " +
                "(ORDERID, STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY, STATUS, SHIPPINGOPTIONS) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            Address a = s.getDestination();
            ps.setInt(1, s.getOrderId());
            ps.setString(2, a.getNumber());
            ps.setString(3, a.getStreetName());
            ps.setString(4, a.getSuburb());
            ps.setString(5, a.getPostcode());
            ps.setString(6, a.getCity());
            ps.setString(7, s.getStatus());
            ps.setString(8, s.getShippingOptions());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int newId = rs.getInt(1);
                    return findById(newId);
                }
            }
        }
        return null;
    }

    /** Read by shipment ID */
    @Override
    public Shipment get(Shipment s) throws SQLException {
        return findById(s.getShipmentId());
    }

    public Shipment findById(int id) throws SQLException {
        String sql = "SELECT * FROM SHIPMENTS WHERE ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) return mapRow(r);
            }
        }
        return null;
    }

    /** List all shipments for a given order */
    public List<Shipment> listByOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM SHIPMENTS WHERE ORDERID = ?";
        List<Shipment> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) {
                    list.add(mapRow(r));
                }
            }
        }
        return list;
    }

    /** Update a shipment’s details */
    @Override
    public void update(Shipment oldS, Shipment newS) throws SQLException {
        String sql = "UPDATE SHIPMENTS SET " +
                "STREETNUMBER=?, STREETNAME=?, SUBURB=?, POSTCODE=?, CITY=?, STATUS=?, SHIPPINGOPTIONS=? " +
                "WHERE ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            Address a = newS.getDestination();
            ps.setString(1, a.getNumber());
            ps.setString(2, a.getStreetName());
            ps.setString(3, a.getSuburb());
            ps.setString(4, a.getPostcode());
            ps.setString(5, a.getCity());
            ps.setString(6, newS.getStatus());
            ps.setString(7, newS.getShippingOptions());
            ps.setInt(8, oldS.getShipmentId());
            ps.executeUpdate();
        }
    }

    /** Delete a shipment record */
    @Override
    public void delete(Shipment s) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM SHIPMENTS WHERE ID = ?")) {
            ps.setInt(1, s.getShipmentId());
            ps.executeUpdate();
        }
    }

    private Shipment mapRow(ResultSet r) throws SQLException {
        int id      = r.getInt("ID");
        int oid     = r.getInt("ORDERID");
        Address a   = new Address(
                r.getString("STREETNUMBER"),
                r.getString("STREETNAME"),
                r.getString("SUBURB"),
                r.getString("POSTCODE"),
                r.getString("CITY")
        );
        String st   = r.getString("STATUS");
        String opts = r.getString("SHIPPINGOPTIONS");
        return new Shipment(oid, a, st, opts);
    }
}
