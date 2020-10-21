package datasource;

import datasource.datamapper.OwnerDataMapper;
import domain.objects.Owner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OwnerDataMapperTest {

    private OwnerDataMapper ownerDataMapper;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private DatabaseConnector databaseConnectorMock;
    @Mock private Connection connectionMock;
    @Mock private Statement statementMock;
    @Mock private ResultSet resultSetMock;

    @Before
    public void initialize(){
        ownerDataMapper = new OwnerDataMapper(databaseConnectorMock);
    }

    @Test
    public void test_read_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery("select * from owner where username = 'user1'")).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(true);
            when(resultSetMock.getString("username")).thenReturn("user1");
            when(resultSetMock.getString("password")).thenReturn("abcdefg");
            when(resultSetMock.getString("token")).thenReturn("1234-1234-1234");

            Owner owner = ownerDataMapper.read("user1");

            assertEquals("abcdefg" ,owner.getPassword());
            assertEquals("user1" ,owner.getUsername());
            assertEquals("1234-1234-1234" ,owner.getToken());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_readByToken_method()  {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery("select * from owner where token = '1234-1234-1234'")).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(true);
            when(resultSetMock.getString("username")).thenReturn("user1");
            when(resultSetMock.getString("password")).thenReturn("abcdefg");

            Owner owner = ownerDataMapper.readByToken("1234-1234-1234");

            assertEquals("abcdefg", owner.getPassword());
            assertEquals("user1", owner.getUsername());
            assertEquals("1234-1234-1234", owner.getToken());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_update_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            ownerDataMapper.update("a","b","c");

            verify(statementMock).execute("update owner set username = 'a', password = 'b' , token = 'c' where username = 'a'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
