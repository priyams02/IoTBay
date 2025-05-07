package uts.isd.model.DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;


public class DAO {
    ArrayList<AbstractDBManager<?>> tables;
    Connection connection;

    public DAO() throws SQLException {
        tables = new ArrayList<>();
        connection = new DBConnector().getConnection();
        try {
            tables.add(new UserDBManager(connection));
        }
        catch (SQLException ex) {
            System.out.println("Error initializing DBManagers");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public UserDBManager Users() {
        return (UserDBManager) tables.get(0);
    }

}