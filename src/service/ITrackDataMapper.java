package service;

import domain.objects.Track;
import java.util.List;

public interface ITrackDataMapper {

    Track read(int id);
    void updateOfflineAvailable(Track track);
    List<Track> readAllExcept(int exceptForPlaylist);
    List<Track> readByPlayList(int playListID);
    void removeTrackFromPlayList(int playListID, int trackID);
    void addTrackToPlayList(int playListID, Track track);
}
