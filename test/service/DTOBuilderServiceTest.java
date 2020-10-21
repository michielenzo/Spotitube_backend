package service;

import domain.objects.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rest.dto.AllPlayListsResponse;
import rest.dto.GetTrackListResponse;

import java.util.ArrayList;

public class DTOBuilderServiceTest {

    private DTOBuilderService dtoBuilderService;

    @Before
    public void initialize(){
        dtoBuilderService = new DTOBuilderService();
    }

    @Test
    public void test_buildAllPlayListsResponseDTO_method(){

        ArrayList<PlayList> allPlayLists = new ArrayList<>();

        Track track1 = new Song(); track1.setDuration(50);
        Track track2 = new Song(); track2.setDuration(50);
        Track track3 = new Song(); track3.setDuration(50);

        ArrayList<Track> trackList = new ArrayList<>();
        trackList.add(track1);
        trackList.add(track2);
        trackList.add(track3);

        PlayList playList = new PlayList();
        playList.setOwner(new Owner());
        playList.setId(1);
        playList.setTrackList(trackList);

        allPlayLists.add(playList);

        AllPlayListsResponse response = dtoBuilderService.buildAllPlayListsResponseDTO(allPlayLists, "1234");

        Assert.assertNotNull(response.getPlaylists());
        Assert.assertEquals(150, response.getLength());
        Assert.assertEquals(3 , response.getPlaylists().get(0).getTracks().size());
    }

    @Test
    public void test_buildGetAllTracksResponseDTO_method(){

        Track track = new Song();
        Track track2 = new Song();
        Track track3 = new Video();

        ArrayList<Track> tracks = new ArrayList<>();
        tracks.add(track); tracks.add(track2); tracks.add(track3);

        GetTrackListResponse response = dtoBuilderService.buildGetAllTracksResponseDTO(tracks);

        Assert.assertEquals(3, response.getTracks().size());
    }
}
