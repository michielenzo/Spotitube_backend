package service;

import domain.objects.Track;

import java.util.List;

public interface ITrackService {
    List<Track> getAllTracks(int forPlaylist);
    List<Track> getAllTracksByPlayList(int playListID);
    void removeTrackFromPlayList(int playListID, int trackID);
    void addTrackToPlayList(int playListID, Track track);
    void updateOfflineAvailable(Track track);
}
