import datasource.ConfigReader;
import datasource.DatabaseConnector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


public class DatabaseConnectorTest {

    private DatabaseConnector connector;

    @Rule public MockitoRule rule = MockitoJUnit.rule();

    @Before
    public void initialize(){
        connector = new DatabaseConnector(new ConfigReader());
    }

    @Test
    public void testConnection(){
        Assert.assertNotNull(connector.getConnection());
    }
}
