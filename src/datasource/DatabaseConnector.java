package datasource;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;

import javax.inject.Inject;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnector implements IDatabaseConnector{

    private ConfigReader config;

    @Inject
    public DatabaseConnector(ConfigReader config){ this.config = config; }

    @Override
    public Cluster getConnection() {
        return Cluster.connect(
                config.read("connection_string"),
                config.read("username"),
                config.read("password"));
    }
}
