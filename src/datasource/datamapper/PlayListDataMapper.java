package datasource.datamapper;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import com.sun.applet2.preloader.event.DownloadErrorEvent;
import datasource.IDatabaseConnector;
import datasource.DatabaseConnector;
import domain.objects.*;
import service.IPlayListDataMapper;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "SELECT MAX(playlists.id) as max " +
                          "FROM spotitube.main.`User` UNNEST playlists",
                QueryOptions.queryOptions().parameters(JsonArray.from(username)));
        int maxID = maxIdResult.rowsAsObject().get(0).getInt("max");

        cluster.query(
                "UPDATE spotitube.main.`User` " +
                          "SET playlists = ARRAY_APPEND(playlists, {'name':?, 'id':?}) " +
                          "WHERE username =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(name, maxID + 1, username)));
    }

    public void updateName(PlayList playList){
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "UPDATE spotitube.main.`User` " +
                          "SET pl.name =? " +
                          "FOR pl IN playlists WHEN pl.id =? END",
                QueryOptions.queryOptions().parameters(JsonArray.from(playList.getName(), playList.getId())));
    }

    public void delete(int id){
        Cluster cluster = databaseConnector.getConnection();

        cluster.query(
                "UPDATE spotitube.main.`User` " +
                          "SET playlists = ARRAY pl " +
                          "FOR pl IN playlists WHEN pl.id != ? END",
                QueryOptions.queryOptions().parameters(JsonArray.from(id)));
    }

    public List<PlayList> readAll(String token){
        Cluster cluster = databaseConnector.getConnection();

        QueryResult resultUser = cluster.query(
                "SELECT *" +
                          "FROM spotitube.main.`User`" +
                          "WHERE token = ?",
                QueryOptions.queryOptions().parameters(JsonArray.from(token)));

        JsonObject resultJson = resultUser.rowsAsObject().get(0).getObject("User");

        List<PlayList> allPlaylistList = new ArrayList<>();

        Owner owner = new Owner();
        owner.setUsername(resultJson.getString("username"));
        owner.setPassword(resultJson.getString("password"));
        owner.setToken(token);

        for (Object playlistJson : ((JsonArray)resultJson.get("playlists"))){
            PlayList playlist = new PlayList();
            playlist.setOwner(owner);

            playlist.setName(((JsonObject)playlistJson).getString("name"));
            playlist.setId(((JsonObject)playlistJson).getInt("id"));

            ArrayList<Track> trackList = new ArrayList<>();

            JsonArray tracksIds = ((JsonObject) playlistJson).getArray("tracksId");

            for(int i = 0; i < tracksIds.size(); i++){
                System.out.println(tracksIds.get(i));
                QueryResult resultTrack = cluster.query(
                        "SELECT *" +
                                  "FROM spotitube.main.`Track`" +
                                  "WHERE id =?",
                        QueryOptions.queryOptions().parameters(JsonArray.from(tracksIds.get(i))));

                JsonObject trackJson = resultTrack.rowsAsObject().get(0).getObject("Track");

                Track track;
                if(isSong(trackJson)){
                    track = new Song();
                    ((Song)track).setAlbum(trackJson.getString("album"));
                }else{
                    track = new Video();
                    ((Video)track).setPublicationDate(trackJson.getString("publicationdate"));
                    ((Video)track).setDescription(trackJson.getString("description"));
                }

                track.setDuration(trackJson.getInt("duration"));
                track.setId(trackJson.getInt("id"));
                track.setPerformer(trackJson.getString("performer"));
                track.setTitle(trackJson.getString("title"));
                track.setDuration(trackJson.getInt("duration"));
                track.setOfflineAvailable(trackJson.getBoolean("offlineavailable"));
                track.setPlayCount(trackJson.getInt("playcount"));

                trackList.add(track);
            }

            playlist.setTrackList(trackList);

            allPlaylistList.add(playlist);
        }

        return allPlaylistList;
    }

    private boolean isSong(JsonObject resultSet){
        return resultSet.getString("album") != null;
    }
}
