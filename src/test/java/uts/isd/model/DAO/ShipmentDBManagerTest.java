package uts.isd.model.DAO;

import junit.framework.TestCase;
import uts.isd.model.Shipment;
import uts.isd.model.Person.Address;

import java.sql.*;
import java.util.List;

public class ShipmentDBManagerTest extends TestCase {

    private Connection conn;
    private ShipmentDBManager mgr;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // In-memory SQLite
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        try (Statement st = conn.createStatement()) {
            String ddl =
                    "CREATE TABLE SHIPMENTS (" +
                            " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            " ORDERID INTEGER," +
                            " STREETNUMBER TEXT," +
                            " STREETNAME TEXT," +
                            " SUBURB TEXT," +
                            " POSTCODE TEXT," +
                            " CITY TEXT," +
                            " STATUS TEXT," +
                            " SHIPPINGOPTIONS TEXT" +
                            ");";
            st.execute(ddl);
        }
        mgr = new ShipmentDBManager(conn);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    public void testAddAndListByOrder() throws Exception {
        Address addr = new Address("123", "Main St", "Suburbia", "2000", "Sydney");
        Shipment in = new Shipment(42, addr, "Pending", "Express");
        Shipment saved = mgr.add(in);
        assertTrue("Should generate an ID", saved.getShipmentId() > 0);

        List<Shipment> list = mgr.listByOrder(42);
        assertEquals(1, list.size());
        Shipment out = list.get(0);
        assertEquals("Express", out.getShippingOptions());
        assertEquals("2000", out.getDestination().getPostcode());
    }

    public void testUpdateAndFindById() throws Exception {
        Shipment s = mgr.add(new Shipment(7,
                new Address("10", "Original Rd", "Test", "3000", "Melb"),
                "New", "Standard"
        ));

        Shipment updated = new Shipment(7,
                new Address("11", "Changed Rd", "Elsewhere", "4000", "Bris"),
                "Shipped", "Overnight"
        );
        mgr.update(s, updated);

        Shipment reloaded = mgr.findById(s.getShipmentId());
        assertEquals("11", reloaded.getDestination().getNumber());
        assertEquals("Shipped", reloaded.getStatus());
    }

    public void testDeleteRemovesRecord() throws Exception {
        Shipment s = mgr.add(new Shipment(99,
                new Address("5", "Delete St", "Gone", "5001", "Perth"),
                "X", "Y"
        ));
        mgr.delete(s);
        List<Shipment> after = mgr.listByOrder(99);
        assertTrue("No shipments after delete", after.isEmpty());
    }
}
