package datasource.datamapper;

import datasource.IDatabaseConnector;
import domain.objects.Song;
import domain.objects.Track;
import domain.objects.Video;
import service.ITrackDataMapper;

import javax.inject.Inject;
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
//        try {
//            Statement stmt = databaseConnector.getConnection().createStatement();
//            String query = String.format("select * from track where trackid = %s", id);
//            ResultSet resultSet = stmt.executeQuery(query);
//            if(resultSet.next()){
//                track = buildTrack(resultSet);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return track;
    }

    public void update(Track track){
//        try {
//            Statement stmt = databaseConnector.getConnection().createStatement();
//            int bit = 0;
//            if(track.isOfflineAvailable()){ bit = 1; }
//            String query = String.format("update track set performer = '%s'," +
//                    "title = '%s'," + "playcount = %s," +
//                    "duration = %s," + "offlineavailable = %s,",
//                    track.getPerformer(), track.getTitle(),
//                    track.getPlayCount(), track.getDuration(),
//                    bit);
//            if(track instanceof Song){
//                query += String.format("album = '%s'",
//                        ((Song)track).getAlbum());
//            }else {
//                query += String.format("publicationdate = '%s'," +
//                        "description = '%s'",
//                        ((Video)track).getPublicationDate(),
//                        ((Video)track).getDescription());
//            }
//            query += String.format(" where trackid = %s", track.getId());
//            stmt.execute(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public List<Track> readAll(int exceptForPlaylist) {
        List<Track> trackList = new ArrayList<>();
//        try {
//            Statement stmt = databaseConnector.getConnection().createStatement();
//            String query;
//            if(exceptForPlaylist == 0){
//                query = "select * from track";
//            }else {
//                query = String.format("select *\n" +
//                        "from track\n" +
//                        "where trackid not in (\n" +
//                        "\tselect distinct t.trackID\n" +
//                        "\tfrom track t\n" +
//                        "\tleft join trackinplayList tip\n" +
//                        "\ton tip.trackid = t.trackid\n" +
//                        "\twhere tip.playlistid = %s\n" +
//                        ")", exceptForPlaylist);
//            }
//            ResultSet resultSet = stmt.executeQuery(query);
//            while(resultSet.next()){
//                trackList.add(buildTrack(resultSet));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return trackList;
    }

    public List<Track> readByPlayList(int playListID) {
        List<Track> trackList = new ArrayList<>();
//        try {
//            Statement stmt = databaseConnector.getConnection().createStatement();
//            String query = String.format("select * from track t\n" +
//                    "inner join trackinplaylist tip\n" +
//                    "on tip.trackid = t.trackid\n" +
//                    "where tip.playlistid = %s", playListID);
//            ResultSet resultSet = stmt.executeQuery(query);
//            while(resultSet.next()){
//                trackList.add(buildTrack(resultSet));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return trackList;
    }

    public void removeTrackFromPlayList(int playListID, int trackID) {
//        try {
//            Statement stmt = databaseConnector.getConnection().createStatement();
//            String query = String.format("delete from trackinplaylist " +
//                    "where playlistid = %s and trackid = %s", playListID, trackID);
//            stmt.execute(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

    public void addTrackToPlayList(int playListID, Track track) {
//        try {
//            Statement stmt = databaseConnector.getConnection().createStatement();
//            String query = String.format("insert into trackinplaylist values (%s, %s)", playListID, track.getId());
//            stmt.execute(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
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
