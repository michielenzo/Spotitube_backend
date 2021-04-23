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

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class PlaylistDataMapperTest {

    private PlayListDataMapper playListDataMapper;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private DatabaseConnector databaseConnectorMock;
    @Mock private Connection connectionMock;
    @Mock private PreparedStatement statementMock;
    @Mock private ResultSet resultSetMock;

    private static final String ALBUM_NAME = "rock 'n roll";
    private static final int TRACK_ID = 1;
    private static final String PERFORMER = "Andre Hazes";
    private static final String TITLE = "lied";
    private static final int PLAYCOUNT = 235789;
    private static final int DURATION = 435;
    private static final boolean OFFLINE_AVAILABLE = true;
    private static final int PLAYLIST_ID = 1;
    private static final String USERNAME = "user123";
    private static final String PASSWORD = "password";
    private static final String TOKEN = "1234-1234-1234";
    private static final String PLAYLIST_NAME = "l1";

    @Before
    public void initialize(){
        playListDataMapper = new PlayListDataMapper(databaseConnectorMock);
    }

    @Test
    public void test_create_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("insert into playlist (name, username) values (?,?)"))
                    .thenReturn(statementMock);

            playListDataMapper.create(PLAYLIST_NAME, USERNAME);

            verify(statementMock).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_delete_method() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("delete from trackinplaylist where playlistid = ?"))
                    .thenReturn(statementMock);

            when(databaseConnectorMock.getConnection()
                    .prepareStatement("delete from playlist where playlistid = ?"))
                    .thenReturn(statementMock);

            playListDataMapper.delete(1);

            verify(statementMock, times(2)).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_update_method() {

        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("update playlist set name = ? where playlistid = ?"))
                    .thenReturn(statementMock);

            PlayList playList = new PlayList();
            playList.setId(TRACK_ID);
            playList.setName(PLAYLIST_NAME);

            playListDataMapper.update(playList);

            verify(statementMock).execute();
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

            when(resultSetMock.getInt("playlistid")).thenReturn(PLAYLIST_ID);
            when(resultSetMock.getString("username")).thenReturn(USERNAME);
            when(resultSetMock.getString("password")).thenReturn(PASSWORD);
            when(resultSetMock.getString("token")).thenReturn(TOKEN);
            when(resultSetMock.getString("album")).thenReturn(ALBUM_NAME);
            when(resultSetMock.getInt("trackid")).thenReturn(TRACK_ID);
            when(resultSetMock.getString("performer")).thenReturn(PERFORMER);
            when(resultSetMock.getString("title")).thenReturn(TITLE);
            when(resultSetMock.getInt("playcount")).thenReturn(PLAYCOUNT);
            when(resultSetMock.getInt("duration")).thenReturn(DURATION);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(OFFLINE_AVAILABLE);

            List<PlayList> playListList = playListDataMapper.readAll();

            assertEquals(TITLE, playListList.get(0).getTrackList().get(0).getTitle());
            assertEquals(DURATION, playListList.get(0).getTrackList().get(0).getDuration());
            assertEquals(PERFORMER, playListList.get(0).getTrackList().get(0).getPerformer());
            assertEquals(PLAYCOUNT ,playListList.get(0).getTrackList().get(0).getPlayCount());
            assertEquals(TRACK_ID ,playListList.get(0).getTrackList().get(0).getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
