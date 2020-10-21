package datasource;

import datasource.datamapper.TrackDataMapper;
import domain.objects.Song;
import domain.objects.Track;
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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TrackDataMapperTest {

    private TrackDataMapper trackDataMapper;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private MySQLConnector databaseConnectorMock;
    @Mock private Connection connectionMock;
    @Mock private Statement statementMock;
    @Mock private ResultSet resultSetMock;

    @Before
    public void initialize(){
        trackDataMapper = new TrackDataMapper(databaseConnectorMock);
    }

    @Test
    public void testThat_Read_MethodCanBuildA_Song_DomainObject() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            when(statementMock.executeQuery("select * from track where trackid = 1")).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true);
            when(resultSetMock.getString("album")).thenReturn("bloemetjes");
            when(resultSetMock.getInt("trackid")).thenReturn(1);
            when(resultSetMock.getString("performer")).thenReturn("Andre Hazes");
            when(resultSetMock.getString("title")).thenReturn("boterbloem");
            when(resultSetMock.getInt("playcount")).thenReturn(235789);
            when(resultSetMock.getInt("duration")).thenReturn(435);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(true);

            Track track = trackDataMapper.read(1);

            assertThat(track, instanceOf(Song.class));
            assertEquals("boterbloem", track.getTitle());
            assertEquals(435, track.getDuration());
            assertEquals("Andre Hazes", track.getPerformer());
            assertEquals(235789 ,track.getPlayCount());
            assertEquals(1 ,track.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_ReadAll_MethodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            when(statementMock.executeQuery("select *\n" +
                    "from track\n" +
                    "where trackid not in (\n" +
                    "\tselect distinct t.trackID\n" +
                    "\tfrom track t\n" +
                    "\tleft join trackinplayList tip\n" +
                    "\ton tip.trackid = t.trackid\n" +
                    "\twhere tip.playlistid = 1\n" +
                    ")")).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);

            when(resultSetMock.getString("album")).thenReturn("bloemetjes");
            when(resultSetMock.getInt("trackid")).thenReturn(1);
            when(resultSetMock.getString("performer")).thenReturn("Andre Hazes");
            when(resultSetMock.getString("title")).thenReturn("boterbloem");
            when(resultSetMock.getInt("playcount")).thenReturn(235789);
            when(resultSetMock.getInt("duration")).thenReturn(435);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(true);

            List<Track> trackList = trackDataMapper.readAll(1);

            assertEquals("boterbloem" ,trackList.get(0).getTitle());
            assertEquals("boterbloem" ,trackList.get(1).getTitle());
            assertEquals("Andre Hazes" ,trackList.get(0).getPerformer());
            assertEquals("Andre Hazes" ,trackList.get(1).getPerformer());
            assertEquals(1 ,trackList.get(0).getId());
            assertEquals(1 ,trackList.get(1).getId());
            assertEquals(235789 ,trackList.get(0).getPlayCount());
            assertEquals(235789 ,trackList.get(1).getPlayCount());
            assertEquals(435 ,trackList.get(0).getDuration());
            assertEquals(435 ,trackList.get(1).getDuration());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_readByPlaylist_methodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            when(statementMock.executeQuery("select *\n" +
                    "from track\n" +
                    "where trackid not in (\n" +
                    "\tselect distinct t.trackID\n" +
                    "\tfrom track t\n" +
                    "\tleft join trackinplayList tip\n" +
                    "\ton tip.trackid = t.trackid\n" +
                    "\twhere tip.playlistid = 1\n" +
                    ")")).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);

            when(resultSetMock.getString("album")).thenReturn("bloemetjes");
            when(resultSetMock.getInt("trackid")).thenReturn(1);
            when(resultSetMock.getString("performer")).thenReturn("Andre Hazes");
            when(resultSetMock.getString("title")).thenReturn("boterbloem");
            when(resultSetMock.getInt("playcount")).thenReturn(235789);
            when(resultSetMock.getInt("duration")).thenReturn(435);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(true);

            List<Track> trackList = trackDataMapper.readAll(1);

            assertEquals("boterbloem" ,trackList.get(0).getTitle());
            assertEquals("boterbloem" ,trackList.get(1).getTitle());
            assertEquals("Andre Hazes" ,trackList.get(0).getPerformer());
            assertEquals("Andre Hazes" ,trackList.get(1).getPerformer());
            assertEquals(1 ,trackList.get(0).getId());
            assertEquals(1 ,trackList.get(1).getId());
            assertEquals(235789 ,trackList.get(0).getPlayCount());
            assertEquals(235789 ,trackList.get(1).getPlayCount());
            assertEquals(435 ,trackList.get(0).getDuration());
            assertEquals(435 ,trackList.get(1).getDuration());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_removeTrackFromPlaylistWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            trackDataMapper.removeTrackFromPlayList(1,1);

            verify(statementMock).execute("delete from trackinplaylist " +
                    "where playlistid = 1 and trackid = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_addTrackToPlaylist_methodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            Track track = new Song();
            track.setId(1);

            trackDataMapper.addTrackToPlayList(1,track);

            verify(statementMock).execute("insert into trackinplaylist values (1, 1)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_update_methodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection().createStatement()).thenReturn(statementMock);

            Song track = new Song();
            track.setId(1);
            track.setDuration(22);
            track.setOfflineAvailable(true);
            track.setPerformer("a");
            track.setTitle("b");
            track.setAlbum("c");

            trackDataMapper.update(track);

            verify(statementMock).execute("update track set performer = 'a'," +
                    "title = 'b'," + "playcount = 0," +
                    "duration = 22," + "offlineavailable = 1,album = 'c' where trackid = 1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
