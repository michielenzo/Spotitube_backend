import com.mysql.cj.protocol.Resultset;
import datasource.ConfigReader;
import datasource.DatabaseConnector;
import org.apache.calcite.sql.SqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnectorTest {

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

    @Test
    public void testPostgresqlConnection(){
        Mockito.when(configReader.read("driver")).thenReturn("org.postgresql.Driver");
        Mockito.when(configReader.read("connection_string"))
                .thenReturn("jdbc:postgresql://localhost:5432/spotitube?user=postgres&password=12345");

        Connection connection = connector.getConnection();

        //delete this
        try {
            Statement stmt = connection.createStatement();
            stmt.execute("update owner set username = 'mo', password = '12345' , token = 'blablabla' where username = 'mo'");
//            ResultSet result = stmt.executeQuery("select * from owner where username = 'mo'");
//            result.next();
//            System.out.println(result.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Assert.assertNotNull(connection);
    }

    @Test
    public void test() throws SqlParseException {
        connector.getConnection();
    }
}
