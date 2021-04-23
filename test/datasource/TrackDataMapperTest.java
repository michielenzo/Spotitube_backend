package datasource;

import datasource.datamapper.TrackDataMapper;
import domain.objects.Song;
import domain.objects.Track;
import domain.objects.Video;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.exceptions.stacktrace.ConditionalStackTraceFilter;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.sql.*;
import java.time.Period;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TrackDataMapperTest {

    private TrackDataMapper trackDataMapper;

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
    private static final String PUBLICATION_DATE = "01-01-2020";
    private static final String DESCRIPTION = "lorem ipsum";
    private static final int PLAYLIST_ID = 1;

    @Before
    public void initialize(){
        trackDataMapper = new TrackDataMapper(databaseConnectorMock);
    }

    @Test
    public void testThat_Read_MethodCanBuildA_Song_DomainObject() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("select * from track where trackid = ?"))
                    .thenReturn(statementMock);

            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true);
            when(resultSetMock.getString("album")).thenReturn(ALBUM_NAME);
            when(resultSetMock.getInt("trackid")).thenReturn(TRACK_ID);
            when(resultSetMock.getString("performer")).thenReturn(PERFORMER);
            when(resultSetMock.getString("title")).thenReturn(TITLE);
            when(resultSetMock.getInt("playcount")).thenReturn(PLAYCOUNT);
            when(resultSetMock.getInt("duration")).thenReturn(DURATION);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(OFFLINE_AVAILABLE);

            Track track = trackDataMapper.read(1);

            assertThat(track, instanceOf(Song.class));
            assertEquals(TITLE, track.getTitle());
            assertEquals(DURATION, track.getDuration());
            assertEquals(PERFORMER, track.getPerformer());
            assertEquals(PLAYCOUNT ,track.getPlayCount());
            assertEquals(TRACK_ID ,track.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_ReadAll_MethodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement(
                            "select * from track " +
                            "where trackid not in ( " +
                            "select distinct t.trackID " +
                            "from track t " +
                            "left join trackinplayList tip " +
                            "on tip.trackid = t.trackid " +
                            "where tip.playlistid = ?)"))
                    .thenReturn(statementMock);

            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(false);

            when(resultSetMock.getString("album")).thenReturn(ALBUM_NAME);
            when(resultSetMock.getInt("trackid")).thenReturn(TRACK_ID);
            when(resultSetMock.getString("performer")).thenReturn(PERFORMER);
            when(resultSetMock.getString("title")).thenReturn(TITLE);
            when(resultSetMock.getInt("playcount")).thenReturn(PLAYCOUNT);
            when(resultSetMock.getInt("duration")).thenReturn(DURATION);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(OFFLINE_AVAILABLE);

            List<Track> trackList = trackDataMapper.readAll(1);

            assertEquals(TITLE ,trackList.get(0).getTitle());
            assertEquals(TITLE ,trackList.get(1).getTitle());
            assertEquals(PERFORMER ,trackList.get(0).getPerformer());
            assertEquals(PERFORMER ,trackList.get(1).getPerformer());
            assertEquals(TRACK_ID ,trackList.get(0).getId());
            assertEquals(TRACK_ID ,trackList.get(1).getId());
            assertEquals(PLAYCOUNT ,trackList.get(0).getPlayCount());
            assertEquals(PLAYCOUNT ,trackList.get(1).getPlayCount());
            assertEquals(DURATION ,trackList.get(0).getDuration());
            assertEquals(DURATION ,trackList.get(1).getDuration());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_readByPlaylist_methodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement(
                            "select * from track t " +
                            "inner join trackinplaylist tip " +
                            "on tip.trackid = t.trackid " +
                            "where tip.playlistid = ?")).thenReturn(statementMock);

            when(statementMock.executeQuery()).thenReturn(resultSetMock);

            when(resultSetMock.next()).thenReturn(true).thenReturn(false);

            when(resultSetMock.getString("album")).thenReturn(null);
            when(resultSetMock.getString("publicationdate")).thenReturn(PUBLICATION_DATE);
            when(resultSetMock.getString("description")).thenReturn(DESCRIPTION);
            when(resultSetMock.getInt("trackid")).thenReturn(TRACK_ID);
            when(resultSetMock.getString("performer")).thenReturn(PERFORMER);
            when(resultSetMock.getString("title")).thenReturn(TITLE);
            when(resultSetMock.getInt("playcount")).thenReturn(PLAYCOUNT);
            when(resultSetMock.getInt("duration")).thenReturn(DURATION);
            when(resultSetMock.getBoolean("offlineavailable")).thenReturn(OFFLINE_AVAILABLE);

            List<Track> tracks = trackDataMapper.readByPlayList(1);

            assertEquals(PUBLICATION_DATE, ((Video)tracks.get(0)).getPublicationDate());
            assertEquals(DESCRIPTION, ((Video)tracks.get(0)).getDescription());
            assertEquals(TRACK_ID, tracks.get(0).getId());
            assertEquals(PERFORMER, tracks.get(0).getPerformer());
            assertEquals(TITLE, tracks.get(0).getTitle());
            assertEquals(PLAYCOUNT ,tracks.get(0).getPlayCount());
            assertEquals(DURATION, tracks.get(0).getDuration());
            assertTrue(tracks.get(0).isOfflineAvailable());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_removeTrackFromPlaylistWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("delete from trackinplaylist where playlistid = ? and trackid = ?"))
                    .thenReturn(statementMock);

            trackDataMapper.removeTrackFromPlayList(PLAYLIST_ID, TRACK_ID);

            verify(statementMock).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_addTrackToPlaylist_methodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement("insert into trackinplaylist values (?, ?)"))
                    .thenReturn(statementMock);

            Track track = new Song();
            track.setId(TRACK_ID);

            trackDataMapper.addTrackToPlayList(PLAYLIST_ID, track);

            verify(statementMock).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testThat_update_methodWorks() {
        try {
            when(databaseConnectorMock.getConnection()).thenReturn(connectionMock);
            when(databaseConnectorMock.getConnection()
                    .prepareStatement(
                     "update track set performer = ?,title = ?,playcount = ?,duration = ?,offlineavailable = ?, album = ? where trackid = ?"))
                    .thenReturn(statementMock);

            Song track = new Song();
            track.setId(TRACK_ID);
            track.setDuration(22);
            track.setOfflineAvailable(true);
            track.setPerformer("a");
            track.setTitle("b");
            track.setAlbum("c");

            trackDataMapper.update(track);

            verify(statementMock).execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
