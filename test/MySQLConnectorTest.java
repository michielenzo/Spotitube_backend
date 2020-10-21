import datasource.IDatabaseConnector;
import datasource.MySQLConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

public class MySQLConnectorTest {

    private IDatabaseConnector connector;

    @Before
    public void initialize(){
        connector = new MySQLConnector();
    }

    @Test
    public void testMySQLConnection(){
        Connection connection = connector.getConnection();
        Assert.assertNotNull(connection);
    }
}
