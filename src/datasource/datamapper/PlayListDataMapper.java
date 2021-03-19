package datasource.datamapper;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import datasource.IDatabaseConnector;
import datasource.DatabaseConnector;
import domain.objects.*;
import service.IPlayListDataMapper;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PlayListDataMapper implements IPlayListDataMapper {

    private IDatabaseConnector databaseConnector;

    @Inject
    public PlayListDataMapper(DatabaseConnector mySQLConnector){
        this.databaseConnector = mySQLConnector;
    }

    public void create(String name, String username){
        Cluster cluster = databaseConnector.getConnection();

        QueryResult maxIdResult = cluster.query(
                "SELECT MAX(id) AS max " +
                          "FROM spotitube.main.Playlist");
        int maxID = maxIdResult.rowsAsObject().get(0).getInt("max");

        cluster.query(
                "INSERT INTO spotitube.main.Playlist (KEY, VALUE)\n" +
                          "VALUES (\"playlist4\", { \"id\":?, \"name\":?, \"tracksId\":[]})",
                QueryOptions.queryOptions().parameters(JsonArray.from(maxID + 1, name)));

        cluster.query(
                "UPDATE spotitube.main.`User`\n" +
                          "SET playlistIds = ARRAY_APPEND(playlistIds, ?)\n" +
                          "WHERE username =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(maxID + 1, username)));
    }

    public void updateName(PlayList playList){
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "UPDATE spotitube.main.`Playlist` " +
                          "SET name =? " +
                          "WHERE id =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(playList.getName(), playList.getId())));
    }

    public void delete(int id){
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "DELETE FROM spotitube.main.Playlist " +
                          "WHERE id =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(id)));

        cluster.query(
                "UPDATE spotitube.main.`User` " +
                          "SET playlistIds = ARRAY_REMOVE(playlistIds, ?)",
                QueryOptions.queryOptions().parameters(JsonArray.from(id)));
    }

    public List<PlayList> readAllFromUser(String token){
        Cluster cluster = databaseConnector.getConnection();

        QueryResult resultUser = cluster.query(
                "SELECT *" +
                          "FROM spotitube.main.`User`" +
                          "WHERE token = ?",
                QueryOptions.queryOptions().parameters(JsonArray.from(token)));

        JsonObject resultJson = resultUser.rowsAsObject().get(0).getObject("User");

        Owner owner = new Owner();
        owner.setUsername(resultJson.getString("username"));
        owner.setPassword(resultJson.getString("password"));
        owner.setToken(token);

        JsonArray playlistIdsJsonArray = resultJson.getArray("playlistIds");

        List<PlayList> allPlaylistList = new ArrayList<>();

        for(int i = 0; i < playlistIdsJsonArray.size(); i++){
            PlayList playlist = new PlayList();
            playlist.setOwner(owner);

            QueryResult queryResult2 = cluster.query(
                    "SELECT * " +
                              "FROM spotitube.main.Playlist " +
                              "WHERE id =?",
                    QueryOptions.queryOptions().parameters(JsonArray.from(playlistIdsJsonArray.get(i))));

            JsonObject playlistJson = queryResult2.rowsAsObject().get(0).getObject("Playlist");

            playlist.setId(playlistJson.getInt("id"));
            playlist.setName(playlistJson.getString("name"));

            ArrayList<Track> trackList = new ArrayList<>();
            JsonArray trackIdsJsonArray = playlistJson.getArray("tracksId");

            for(int j = 0; j < trackIdsJsonArray.size(); j++){
                QueryResult queryResult3 = cluster.query(
                        "SELECT * " +
                                  "FROM spotitube.main.Track " +
                                  "WHERE id =?",
                        QueryOptions.queryOptions().parameters(JsonArray.from(trackIdsJsonArray.get(j))));

                JsonObject trackJson = queryResult3.rowsAsObject().get(0).getObject("Track");
                trackList.add(Track.fromJson(trackJson));
            }

            playlist.setTrackList(trackList);

            allPlaylistList.add(playlist);
        }

        return allPlaylistList;
    }
}
