package datasource;

import java.sql.Connection;

public interface IDatabaseConnector {

    Connection getConnection();
}

