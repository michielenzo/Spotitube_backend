package service;

import domain.objects.PlayList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rest.dto.ChangePlayListNameRequest;
import rest.dto.Track;

import java.util.ArrayList;

public class DTOToDomainServiceTest {

    private IDTOToDomainService dtoToDomainService;

    @Before
    public void initialize(){
        dtoToDomainService = new DTOToDomainService();
    }

    @Test
    public void test_convertTrackDTOToTrack_method(){
        Track track = new Track();
        track.setId(1);
        track.setDuration(1);
        track.setPlaycount(1);
        track.setTitle("a");
        track.setOfflineAvailable(true);
        track.setPerformer("b");

        domain.objects.Track song = dtoToDomainService.convertTrackDTOToTrack(track);

        Assert.assertEquals(1, song.getId());
        Assert.assertEquals(1, song.getDuration());
        Assert.assertEquals(1, song.getPlayCount());
        Assert.assertEquals("a", song.getTitle());
        Assert.assertEquals("b", song.getPerformer());
    }

    @Test
    public void test_convertPlayListDTOToPlayList_method(){
        ArrayList<Track> trackDTOList = new ArrayList<>();
        Track trackDTO = new Track();
        trackDTOList.add(trackDTO);

        ChangePlayListNameRequest request = new ChangePlayListNameRequest();
        request.setId(1);
        request.setName("name");
        request.setOwner(true);
        request.setTracks(trackDTOList);

        PlayList playList = dtoToDomainService.convertPlayListDTOToPlayList(request);

        Assert.assertEquals(1 ,playList.getId());
        Assert.assertEquals("name" ,playList.getName());
    }
}
