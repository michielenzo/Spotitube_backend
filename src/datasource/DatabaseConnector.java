package datasource;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector implements IDatabaseConnector{

    private ConfigReader configReader;

    @Inject
    public DatabaseConnector(ConfigReader configReader){
        this.configReader = configReader;
    }

    @Override
    public Connection getConnection() {
        try {
            Class.forName(configReader.read("driver")).newInstance();
            return DriverManager.getConnection(configReader.read("connection_string"));
        } catch (Exception ex) {
            System.out.println("[DB conn ERROR]: " + ex.getMessage());
        }
        return null;
    }
}