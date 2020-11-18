package service;

import domain.objects.Track;

import java.sql.ResultSet;
import java.util.List;

public interface ITrackDataMapper {

    Track read(int id);
    void update(Track track);
    List<Track> readAll(int exceptForPlaylist);
    List<Track> readByPlayList(int playListID);
    void removeTrackFromPlayList(int playListID, int trackID);
    void addTrackToPlayList(int playListID, Track track);
}
