import datasource.ConfigReader;
import datasource.DatabaseConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Connection;

public class MySQLConnectorTest {

    private DatabaseConnector connector;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private ConfigReader configReader;

    @Before
    public void initialize(){
        connector = new DatabaseConnector(configReader);
    }

    @Test
    public void testMySQLConnection(){
        Mockito.when(configReader.read("driver")).thenReturn("com.mysql.cj.jdbc.Driver");
        Mockito.when(configReader.read("connection_string"))
                .thenReturn("jdbc:mysql://localhost:3306/spotitube?user=root&password=12345&serverTimezone=UTC");

        Connection connection = connector.getConnection();
        Assert.assertNotNull(connection);
    }
}
