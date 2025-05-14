package uts.isd.model.DAO;

import uts.isd.model.Person.Staff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDBManager extends AbstractDBManager<Staff> {

    public StaffDBManager(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    public Staff add(Staff s) throws SQLException {
        String sql = "INSERT INTO STAFF (EMAIL, FIRSTNAME, LASTNAME, PASSWORD) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, clamp(s.getEmail().toLowerCase(), 100));
            ps.setString(2, clamp(s.getFirstName(), 100));
            ps.setString(3, clamp(s.getLastName(), 100));
            ps.setString(4, clamp(s.getPassword(), 100));
            ps.executeUpdate();
        }
        return s;
    }

    @Override
    public Staff get(Staff s) throws SQLException {
        return findStaff(s.getEmail());
    }

    /** Lookup by email only */
    public Staff findStaff(String email) throws SQLException {
        String sql = "SELECT * FROM STAFF WHERE EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) return resultSetToStaff(r);
            }
        }
        return null;
    }

    /** Login/authentication lookup */
    public Staff findStaff(String email, String password) throws SQLException {
        String sql = "SELECT * FROM STAFF WHERE EMAIL = ? AND PASSWORD = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            ps.setString(2, password);
            try (ResultSet r = ps.executeQuery()) {
                if (r.next()) return resultSetToStaff(r);
            }
        }
        return null;
    }

    /** Search by first name substring (case-insensitive) */
    public List<Staff> searchStaff(String byName) throws SQLException {
        String sql = "SELECT * FROM STAFF WHERE UPPER(FIRSTNAME) LIKE UPPER(?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + byName + "%");
            try (ResultSet r = ps.executeQuery()) {
                List<Staff> list = new ArrayList<>();
                while (r.next()) list.add(resultSetToStaff(r));
                return list;
            }
        }
    }

    /** Load all staff records */
    public List<Staff> listAll() throws SQLException {
        String sql = "SELECT * FROM STAFF";
        try (Statement st = connection.createStatement();
             ResultSet r = st.executeQuery(sql)) {
            List<Staff> list = new ArrayList<>();
            while (r.next()) list.add(resultSetToStaff(r));
            return list;
        }
    }

    @Override
    public void update(Staff oldStaff, Staff newStaff) throws SQLException {
        String sql = "UPDATE STAFF SET FIRSTNAME=?, LASTNAME=?, PASSWORD=?, EMAIL=? WHERE EMAIL=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, clamp(newStaff.getFirstName(), 100));
            ps.setString(2, clamp(newStaff.getLastName(), 100));
            ps.setString(3, clamp(newStaff.getPassword(), 100));
            ps.setString(4, clamp(newStaff.getEmail().toLowerCase(), 100));
            ps.setString(5, oldStaff.getEmail().toLowerCase());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Staff s) throws SQLException {
        String sql = "DELETE FROM STAFF WHERE EMAIL = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, s.getEmail().toLowerCase());
            ps.executeUpdate();
        }
    }

    private Staff resultSetToStaff(ResultSet r) throws SQLException {
        String email     = r.getString("EMAIL");
        String firstName = r.getString("FIRSTNAME");
        String lastName  = r.getString("LASTNAME");
        String password  = r.getString("PASSWORD");
        return new Staff(firstName, lastName, password, email);
    }

    private String clamp(String str, int maxLength) {
        if (str == null) return "";
        return (str.length() <= maxLength) ? str : str.substring(0, maxLength);
    }
}
