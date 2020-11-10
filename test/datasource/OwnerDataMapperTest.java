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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OwnerDataMapperTest {

    private OwnerDataMapper ownerDataMapper;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private DatabaseConnector databaseConnectorMock;
    @Mock private Connection connectionMock;
    @Mock private Statement statementMock;
    @Mock private ResultSet resultSetMock;

    private static final String USERNAME = "user123";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "1234-1234-1234";

    @Before
    public void initialize(){
        ownerDataMapper = new OwnerDataMapper(databaseConnectorMock);
    }

    @Test
    public void test_read_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery("select * from owner where username = 'user123'")).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(true);
            when(resultSetMock.getString("username")).thenReturn(USERNAME);
            when(resultSetMock.getString("password")).thenReturn(PASSWORD);
            when(resultSetMock.getString("token")).thenReturn(TOKEN);

            Owner owner = ownerDataMapper.read(USERNAME);

            assertEquals(PASSWORD ,owner.getPassword());
            assertEquals(USERNAME ,owner.getUsername());
            assertEquals(TOKEN ,owner.getToken());
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
            when(resultSetMock.getString("username")).thenReturn(USERNAME);
            when(resultSetMock.getString("password")).thenReturn(PASSWORD);

            Owner owner = ownerDataMapper.readByToken(TOKEN);

            assertEquals(PASSWORD, owner.getPassword());
            assertEquals(USERNAME, owner.getUsername());
            assertEquals(TOKEN, owner.getToken());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_readByToken_method_returnsNullWhenOwnerDOesNotExist(){
        try{
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);
            when(statementMock.executeQuery("select * from owner where token = '1234-1234-1234'")).thenReturn(resultSetMock);
            when(resultSetMock.next()).thenReturn(false);

            Owner owner = ownerDataMapper.readByToken(TOKEN);

            assertNull(owner);
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void test_update_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            ownerDataMapper.update(USERNAME, PASSWORD,TOKEN);

            verify(statementMock).execute("update owner set username = 'user123', password = 'password' , token = '1234-1234-1234' where username = 'user123'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
