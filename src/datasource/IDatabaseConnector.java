package datasource;


import com.couchbase.client.java.Cluster;

public interface IDatabaseConnector {

    Cluster getConnection();
}

