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

    @Override
    public Shipment add(Shipment s) throws SQLException {
        String sql = "INSERT INTO SHIPMENTS "
                + "(ORDERID, STREETNUMBER, STREETNAME, SUBURB, POSTCODE, CITY, STATUS, SHIPPINGOPTIONS) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
                if (rs.next()) return findById(rs.getInt(1));
            }
        }
        return null;
    }

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

    public List<Shipment> listByOrder(int orderId) throws SQLException {
        String sql = "SELECT * FROM SHIPMENTS WHERE ORDERID = ?";
        List<Shipment> list = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet r = ps.executeQuery()) {
                while (r.next()) list.add(mapRow(r));
            }
        }
        return list;
    }

    @Override
    public void update(Shipment oldS, Shipment newS) throws SQLException {
        String sql = "UPDATE SHIPMENTS SET STREETNUMBER=?, STREETNAME=?, SUBURB=?, POSTCODE=?, "
                + "CITY=?, STATUS=?, SHIPPINGOPTIONS=? WHERE ID = ?";
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

    /** Delete by Shipment object */
    @Override
    public void delete(Shipment s) throws SQLException {
        deleteById(s.getShipmentId());
    }

    /** Delete directly by ID */
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM SHIPMENTS WHERE ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
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
        // Use the constructor that takes the real shipmentId:
         return new Shipment(id, oid, a, st, opts);
        }
}
