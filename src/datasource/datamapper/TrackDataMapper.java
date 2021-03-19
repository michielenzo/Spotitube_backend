package datasource.datamapper;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import datasource.IDatabaseConnector;
import domain.objects.Track;
import service.ITrackDataMapper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TrackDataMapper implements ITrackDataMapper {

    private IDatabaseConnector databaseConnector;

    @Inject
    public TrackDataMapper(IDatabaseConnector databaseConnector){
        this.databaseConnector = databaseConnector;
    }

    public Track read(int id){
        Cluster cluster = databaseConnector.getConnection();

        QueryResult result = cluster.query(
                "SELECT * " +
                          "FROM spotitube.main.`Track` " +
                          "WHERE id =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(id)));

        JsonObject trackJson = result.rowsAsObject().get(0).getObject("Track");

        return Track.fromJson(trackJson);
    }

    public void updateOfflineAvailable(Track track){
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "UPDATE spotitube.main.`Track`" +
                          "SET offlineavailable =? " +
                          "WHERE id =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(track.isOfflineAvailable(), track.getId())));
    }

    public List<Track> readAllExcept(int exceptForTracksInPlaylistWithId_Id) {
        Cluster cluster = databaseConnector.getConnection();

        QueryResult queryResult = cluster.query(
                "SELECT * FROM spotitube.main.Track " +
                          "WHERE id NOT IN (" +
                            "SELECT RAW track " +
                            "FROM spotitube.main.Playlist AS p " +
                            "UNNEST p.tracksId AS track " +
                            "WHERE p.id =?)",
                QueryOptions.queryOptions().parameters(JsonArray.from(exceptForTracksInPlaylistWithId_Id)));

        List<JsonObject> trackListJson = queryResult.rowsAsObject();

        List<Track> trackList = new ArrayList<>();

        for (JsonObject trackJson: trackListJson){
            trackList.add(Track.fromJson(trackJson.getObject("Track")));
        }

        return trackList;
    }

    public List<Track> readByPlayList(int playListID) {
        Cluster cluster = databaseConnector.getConnection();

        QueryResult queryResult = cluster.query(
                "SELECT * FROM spotitube.main.Track " +
                          "WHERE id IN (" +
                            "SELECT RAW track " +
                            "FROM spotitube.main.Playlist AS p " +
                            "UNNEST p.tracksId AS track " +
                            "WHERE p.id =?)",
                QueryOptions.queryOptions().parameters(JsonArray.from(playListID)));

        List<Track> trackList = new ArrayList<>();

        List<JsonObject> trackListJson = queryResult.rowsAsObject();

        for(JsonObject trackJson: trackListJson){
            trackList.add(Track.fromJson(trackJson.getObject("Track")));
        }

        return trackList;
    }

    public void removeTrackFromPlayList(int playListID, int trackID) {
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "UPDATE spotitube.main.Playlist " +
                          "SET tracksId = ARRAY_REMOVE(tracksId, ?) " +
                          "WHERE id =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(trackID, playListID)));
    }

    public void addTrackToPlayList(int playListID, Track track) {
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "UPDATE spotitube.main.Playlist " +
                          "SET tracksId = ARRAY_APPEND(tracksId, ?) " +
                          "WHERE id =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(track.getId(), playListID)));
    }
}
