package datasource.datamapper;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import datasource.IDatabaseConnector;
import domain.objects.Song;
import domain.objects.Track;
import domain.objects.Video;
import service.ITrackDataMapper;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TrackDataMapper implements ITrackDataMapper {

    private IDatabaseConnector databaseConnector;

    @Inject
    public TrackDataMapper(IDatabaseConnector databaseConnector){
        this.databaseConnector = databaseConnector;
    }

    public Track read(int id){
        Track track = null;
        try {
            String query = "select * from track where trackid = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                track = buildTrack(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return track;
    }

    public void update(Track track){
        try {
            String query = String.format(
                    "update track set performer = ?," +
                    "title = ?," + "playcount = ?," +
                    "duration = ?," + "offlineavailable = ?, %s %s",
                    track instanceof Song ? "album = ?" : "publicationdate = ?, description = ?",
                    "where trackid = ?");

            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);

            statement.setString(1, track.getPerformer());
            statement.setString(2, track.getTitle());
            statement.setInt(3, track.getPlayCount());
            statement.setInt(4, track.getDuration());
            statement.setBoolean(5, track.isOfflineAvailable());

            if(track instanceof Song){
                statement.setString(6, ((Song) track).getAlbum());
                statement.setInt(7, track.getId());
            } else {
                statement.setString(6, ((Video) track).getPublicationDate());
                statement.setString(7, ((Video) track).getDescription());
                statement.setInt(8, track.getId());
            }

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Track> readAll(int exceptForPlaylist) {
        List<Track> trackList = new ArrayList<>();
        try {
            String query = exceptForPlaylist == 0
                    ?   "select * from track"
                    :   "select * from track " +
                        "where trackid not in ( " +
                        "select distinct t.trackID " +
                        "from track t " +
                        "left join trackinplayList tip " +
                        "on tip.trackid = t.trackid " +
                        "where tip.playlistid = ?)";

            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                trackList.add(buildTrack(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackList;
    }

    public List<Track> readByPlayList(int playListID) {
        List<Track> trackList = new ArrayList<>();
        try {
            String query =
                    "select * from track t " +
                    "inner join trackinplaylist tip " +
                    "on tip.trackid = t.trackid " +
                    "where tip.playlistid = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setInt(1, playListID);

            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                trackList.add(buildTrack(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trackList;
    }

    public void removeTrackFromPlayList(int playListID, int trackID) {
        try {
            String query = "delete from trackinplaylist where playlistid = ? and trackid = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setInt(1, playListID);
            statement.setInt(2, trackID);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTrackToPlayList(int playListID, Track track) {
        try {
            String query = "insert into trackinplaylist values (?, ?)";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setInt(1, playListID);
            statement.setInt(2, track.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Track buildTrack(ResultSet resultSet) throws SQLException {
        Track track;
        if(isSong(resultSet)){
            track = new Song();
            ((Song) track).setAlbum(resultSet.getString("album"));
        }else{
            track = new Video();
            ((Video) track).setPublicationDate(resultSet.getString("publicationdate"));
            ((Video) track).setDescription(resultSet.getString("description"));
        }
        track.setId(resultSet.getInt("trackid"));
        track.setPerformer(resultSet.getString("performer"));
        track.setTitle(resultSet.getString("title"));
        track.setPlayCount(resultSet.getInt("playcount"));
        track.setDuration(resultSet.getInt("duration"));
        track.setOfflineAvailable(resultSet.getBoolean("offlineavailable"));
        return track;
    }

    private boolean isSong(ResultSet resultSet) throws SQLException {
        return resultSet.getString("album") != null;
    }
}
