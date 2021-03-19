package service;

import domain.objects.Track;

import javax.inject.Inject;
import java.util.List;

public class TrackService implements ITrackService {

    private ITrackDataMapper trackDataMapper;

    @Inject
    public TrackService(ITrackDataMapper trackDataMapper){
        this.trackDataMapper = trackDataMapper;
    }

    @Override
    public List<Track> getAllTracks(int exceptForPlaylist) {
        return trackDataMapper.readAllExcept(exceptForPlaylist);
    }

    @Override
    public List<Track> getAllTracksByPlayList(int playListID) {
        return trackDataMapper.readByPlayList(playListID);
    }

    @Override
    public void removeTrackFromPlayList(int playListID, int trackID) {
        trackDataMapper.removeTrackFromPlayList(playListID, trackID);
    }

    @Override
    public void addTrackToPlayList(int playListID, Track track) {
        trackDataMapper.addTrackToPlayList(playListID, track);
    }

    @Override
    public void updateOfflineAvailable(Track track) {
        trackDataMapper.updateOfflineAvailable(track);
    }
}
