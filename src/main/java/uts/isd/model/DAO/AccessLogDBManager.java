package uts.isd.model.DAO;

import uts.isd.model.AccessLog;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class AccessLogDBManager {
    private final Connection conn;
    public AccessLogDBManager(Connection conn) {
        this.conn = conn;
    }

    /** Fetch every log for this email, newest first */
    public List<AccessLog> findByEmail(String email) throws SQLException {
        String sql = "SELECT LOGID, EMAIL, ACTION, TIMESTAMP FROM ACCESS_LOG "
                + "WHERE EMAIL = ? ORDER BY TIMESTAMP DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                List<AccessLog> list = new ArrayList<>();
                while (rs.next()) {
                    int id      = rs.getInt("LOGID");
                    String act  = rs.getString("ACTION");
                    Timestamp ts= rs.getTimestamp("TIMESTAMP");
                    // convert to LocalDateTime
                    LocalDateTime ldt = ts.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
                    list.add(new AccessLog(id, email, act, ldt));
                }
                return list;
            }
        }
    }

    /** Fetch logs on a specific date (yyyy-MM-dd) */
    public List<AccessLog> findByEmailAndDate(String email, LocalDate date) throws SQLException {
        String sql = "SELECT LOGID, EMAIL, ACTION, TIMESTAMP "
                + "FROM ACCESS_LOG "
                + "WHERE EMAIL=? "
                +   "AND date(TIMESTAMP)=? "
                + "ORDER BY TIMESTAMP DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            ps.setString(2, date.toString());          // ISO: "2025-05-19"
            try (ResultSet rs = ps.executeQuery()) {
                return mapAll(rs);
            }
        }
    }

    private List<AccessLog> mapAll(ResultSet rs) throws SQLException {
        List<AccessLog> list = new ArrayList<>();
        while (rs.next()) {
            int    id    = rs.getInt("LOGID");
            String em    = rs.getString("EMAIL");
            String act   = rs.getString("ACTION");
            Timestamp ts  = rs.getTimestamp("TIMESTAMP");
            LocalDateTime dt = ts.toLocalDateTime();
            list.add(new AccessLog(id, em, act, dt));
        }
        return list;
    }
    /** Inserts a LOGIN or LOGOUT record; TIMESTAMP auto-populates via DEFAULT CURRENT_TIMESTAMP */
    public void addLog(String email, String action) throws SQLException {
        String sql = "INSERT INTO ACCESS_LOG (EMAIL, ACTION) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.toLowerCase());
            ps.setString(2, action);
            ps.executeUpdate();
        }
    }
}