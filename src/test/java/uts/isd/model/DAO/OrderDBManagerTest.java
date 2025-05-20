package uts.isd.model.DAO;

import junit.framework.TestCase;
import uts.isd.model.Order;
import java.sql.*;
import java.util.List;

public class OrderDBManagerTest extends TestCase {
    private Connection conn;
    private OrderDBManager orderDB;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // 1) open in-memory SQLite
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        Statement st = conn.createStatement();
        // 2) create just the tables we need
        st.execute("CREATE TABLE CUSTOMERS (" +
                "EMAIL TEXT PRIMARY KEY," +
                "FIRSTNAME TEXT, LASTNAME TEXT, PASSWORD TEXT," +
                "STREETNUMBER TEXT, STREETNAME TEXT, SUBURB TEXT," +
                "POSTCODE TEXT, CITY TEXT, PHONENUMBER TEXT," +
                "CARDNUMBER TEXT, CVV TEXT, CARDHOLDER TEXT, EXPIRYDATE TEXT" +
                ")");
        st.execute("CREATE TABLE ORDERS (" +
                "ORDERID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "OWNER TEXT, PRICE REAL, STATUS TEXT," +
                "PRODUCTS TEXT, QUANTITY TEXT, PURCHASEDATE TEXT," +
                "STREETNUMBER TEXT, STREETNAME TEXT, SUBURB TEXT," +
                "POSTCODE TEXT, CITY TEXT, CARDNUMBER TEXT," +
                "CVV TEXT, CARDHOLDER TEXT" +
                ")");
        // 3) insert a dummy customer (so foreign lookups work)
        st.execute("INSERT INTO CUSTOMERS(EMAIL,FIRSTNAME,LASTNAME,PASSWORD) " +
                "VALUES('alice@example.com','Alice','Smith','secret')");
        st.close();

        // 4) wire up our DAO
        orderDB = new OrderDBManager(conn);
    }

    public void testFindAndListByOwner() throws Exception {
        // insert a test order
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO ORDERS(OWNER,PRICE,STATUS,PRODUCTS,QUANTITY,PURCHASEDATE," +
                        "STREETNUMBER,STREETNAME,SUBURB,POSTCODE,CITY,CARDNUMBER,CVV,CARDHOLDER)" +
                        " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, "alice@example.com");
        ps.setFloat(2, 99.99f);
        ps.setString(3, "Confirmed");
        ps.setString(4, "");  // no products string
        ps.setString(5, "");
        ps.setString(6, "2025/05/20 12:00:00");
        ps.setString(7, "1");
        ps.setString(8, "Test St");
        ps.setString(9, "MySuburb");
        ps.setString(10, "2000");
        ps.setString(11, "MyCity");
        ps.setString(12, "4111222233334444");
        ps.setString(13, "321");
        ps.setString(14, "Alice S");
        ps.executeUpdate();
        ResultSet keys = ps.getGeneratedKeys();
        assertTrue("should have generated key", keys.next());
        int newId = keys.getInt(1);
        keys.close();
        ps.close();

        // 5) findOrder
        Order o = orderDB.findOrder(newId, "alice@example.com");
        assertNotNull("findOrder should not be null", o);
        assertEquals(newId, o.getId());
        assertEquals(99.99f, o.getTotalCost());

        // 6) listByOwner
        List<Order> list = orderDB.listByOwner("alice@example.com");
        assertEquals("should get exactly 1 order back", 1, list.size());
        assertEquals(newId, list.get(0).getId());
    }

    @Override
    protected void tearDown() throws Exception {
        conn.close();
        super.tearDown();
    }
}
