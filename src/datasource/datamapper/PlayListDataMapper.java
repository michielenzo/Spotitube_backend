package datasource.datamapper;

import datasource.IDatabaseConnector;
import datasource.DatabaseConnector;
import domain.objects.*;
import service.IPlayListDataMapper;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PlayListDataMapper implements IPlayListDataMapper {

    private IDatabaseConnector databaseConnector;

    @Inject
    public PlayListDataMapper(DatabaseConnector mySQLConnector){
        this.databaseConnector = mySQLConnector;
    }

    public void create(String name, String username){
        try {
            String query = "insert into playlist (name, username) values (?,?)";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, username);

            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(PlayList playList){
        try {
            String query = "update playlist set name = ? where playlistid = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, playList.getName());
            statement.setInt(2, playList.getId());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id){
        try {
            String query = "delete from trackinplaylist where playlistid = ?";
            PreparedStatement deleteFromTrackInPlaylistStatement = databaseConnector.getConnection()
                    .prepareStatement(query);
            deleteFromTrackInPlaylistStatement.setInt(1, id);
            deleteFromTrackInPlaylistStatement.execute();

            String query2 = "delete from playlist where playlistid = ?";
            PreparedStatement deleteFromPlaylistStatement = databaseConnector.getConnection()
                    .prepareStatement(query2);
            deleteFromPlaylistStatement.setInt(1, id);
            deleteFromPlaylistStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PlayList> readAll(){
        List<PlayList> allPlaylistList = new ArrayList<>();
        try {
            Statement stmt = databaseConnector.getConnection().createStatement();
            String query = "select pl.playlistid, pl.name, o.username, \n" +
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
                    "order by pl.playlistid";
            ResultSet resultSet = stmt.executeQuery(query);
            int currentID = -1;
            PlayList playList = null;
            while (resultSet.next()){
                if(resultSet.getInt("playlistid") != currentID) {
                    if(playList != null) allPlaylistList.add(playList);
                    playList = new PlayList();
                    playList.setId(resultSet.getInt("playlistid"));
                    playList.setName(resultSet.getString("name"));

                    Owner owner = new Owner();
                    owner.setUsername(resultSet.getString("username"));
                    owner.setPassword(resultSet.getString("password"));
                    owner.setToken(resultSet.getString("token"));
                    playList.setOwner(owner);
                    playList.setTrackList(new ArrayList<Track>());
                    currentID = resultSet.getInt("playlistid");
                }
                if(resultSet.getInt("trackid") > 0) {
                    Track track;
                    if (isSongg(resultSet)) {
                        track = new Song();
                        ((Song) track).setAlbum(resultSet.getString("album"));
                    } else {
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
                    playList.getTrackList().add(track);
                }
            }
            if(playList != null) allPlaylistList.add(playList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allPlaylistList;
    }

    private boolean isSongg(ResultSet resultSet) throws SQLException {
        return resultSet.getString("album") != null;
    }
}
