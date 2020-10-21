package datasource;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLConnector implements IDatabaseConnector{

    @Override
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/spotitube?user=root&password=12345&serverTimezone=UTC");
        } catch (Exception ex) {
            System.out.println("[DB conn ERROR]: " + ex.getMessage());
        }
        return null;
    }
}