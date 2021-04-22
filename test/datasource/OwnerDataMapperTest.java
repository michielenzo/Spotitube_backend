package datasource;

import datasource.datamapper.OwnerDataMapper;
import domain.objects.Owner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OwnerDataMapperTest {

    private OwnerDataMapper ownerDataMapper;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private DatabaseConnector databaseConnectorMock;
    @Mock private Connection connectionMock;
    @Mock private PreparedStatement statementMock;
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
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("select * from owner where username = ?"))
                    .thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
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
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("select * from owner where token = ?"))
                    .thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
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
    public void test_readByToken_method_returnsNullWhenOwnerDoesNotExist(){
        try{
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("select * from owner where token = ?"))
                    .thenReturn(statementMock);
            when(statementMock.executeQuery()).thenReturn(resultSetMock);
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
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("update owner set username = ?, password = ? , token = ? where username = ?"))
                    .thenReturn(statementMock);
            ownerDataMapper.update(USERNAME, PASSWORD,TOKEN);
            verify(statementMock).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
