package datasource;

import datasource.datamapper.OwnerDataMapper;
import datasource.datamapper.PlayListDataMapper;
import datasource.datamapper.TrackDataMapper;
import domain.objects.PlayList;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaylistDataMapperTest {

    private PlayListDataMapper playListDataMapper;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private MySQLConnector databaseConnectorMock;
    @Mock private Connection connectionMock;
    @Mock private Statement statementMock;
    @Mock private ResultSet resultSetMock;

    @Before
    public void initialize(){
        playListDataMapper = new PlayListDataMapper(
                new OwnerDataMapper(databaseConnectorMock),
                new TrackDataMapper(databaseConnectorMock),
                databaseConnectorMock);
    }

    @Test
    public void test_create_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            playListDataMapper.create("a", "b");

            verify(statementMock).execute("insert into playlist values ('a','b')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_delete_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            playListDataMapper.delete(1);

            verify(statementMock).execute("delete from trackinplaylist where playlistid = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_update_method() {

        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            PlayList playList = new PlayList();
            playList.setId(1);
            playList.setName("a");

            playListDataMapper.update(playList);

            verify(statementMock).execute("update playlist set name = 'a' where playlistid = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_readAll_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            when(statementMock.executeQuery(
                    "select pl.playlistid, pl.name, o.username, \n" +
                            "\t   o.password, o.token, \n" +
                            "\t   tip.trackid, t.title,\n" +
                            "\t   t.performer, t.playcount,\n" +
                            "\t   t.duration, t.offlineavailable,\n" +
                            "\t   t.album, t.publicationdate,\n" +
                            "\t   t.description\n" +
                            "from playlist pl\n" +
                            "left join owner o\n" +
                            "on o.username = pl.username\n" +
                            "left join trackinplaylist tip\n" +
                            "on tip.playlistid = pl.playlistid\n" +
                            "left join track t\n" +
                            "on t.trackid = tip.trackid\n" +
                            "order by pl.playlistid"
            )).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true).thenReturn(false);

            when(resultSetMock.getInt("playlistid")).thenReturn(1);
            when(resultSetMock.getString("username")).thenReturn("a");
            when(resultSetMock.getString("password")).thenReturn("wachtwoord");
            when(resultSetMock.getString("token")).thenReturn("1234-1234-1234");
            when(resultSetMock.getString("album")).thenReturn("bloemetjes");
            when(resultSetMock.getInt("trackid")).thenReturn(1);
            when(resultSetMock.getString("performer")).thenReturn("Andre Hazes");
            when(resultSetMock.getString("title")).thenReturn("boterbloem");
            when(resultSetMock.getInt("playcount")).thenReturn(235789);
            when(resultSetMock.getInt("duration")).thenReturn(435);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(true);

            List<PlayList> playListList = playListDataMapper.readAll();

            assertEquals("boterbloem", playListList.get(0).getTrackList().get(0).getTitle());
            assertEquals(435, playListList.get(0).getTrackList().get(0).getDuration());
            assertEquals("Andre Hazes", playListList.get(0).getTrackList().get(0).getPerformer());
            assertEquals(235789 ,playListList.get(0).getTrackList().get(0).getPlayCount());
            assertEquals(1 ,playListList.get(0).getTrackList().get(0).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
