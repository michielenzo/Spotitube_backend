package service;

import datasource.datamapper.TrackDataMapper;
import domain.objects.Track;
import domain.objects.Video;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class TrackServiceTest {

    private TrackService trackService;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private TrackDataMapper trackDataMapper;

    @Before
    public void Initialize(){
        trackService = new TrackService(trackDataMapper);
    }

    @Test
    public void test_getAllTracks_method(){
        trackService.getAllTracks(1);
        Mockito.verify(trackDataMapper).readAllExcept(1);
    }

    @Test
    public void test_getAllTracksByPlayList_method(){
        trackService.getAllTracksByPlayList(1);
        Mockito.verify(trackDataMapper).readByPlayList(1);
    }

    @Test
    public void test_removeTrackFromPlayList(){
        trackService.removeTrackFromPlayList(1,1);
        Mockito.verify(trackDataMapper).removeTrackFromPlayList(1,1);
    }

    @Test
    public void test_addTrackToPlayList(){
        Track track = new Video();
        trackService.addTrackToPlayList(1, track);
        Mockito.verify(trackDataMapper).addTrackToPlayList(1, track);
    }

    @Test
    public void test_updateOfflineAvailable(){
        Track track = new Video();
        trackService.updateOfflineAvailable(track);
        Mockito.verify(trackDataMapper).updateOfflineAvailable(track);
    }
}
