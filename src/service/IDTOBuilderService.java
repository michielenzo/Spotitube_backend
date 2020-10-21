package service;

import domain.objects.PlayList;
import domain.objects.Track;
import rest.dto.AllPlayListsResponse;
import rest.dto.GetTrackListResponse;

import java.util.List;

public interface IDTOBuilderService {
    AllPlayListsResponse buildAllPlayListsResponseDTO(List<PlayList> allPlayLists, String token);
    GetTrackListResponse buildGetAllTracksResponseDTO(List<Track> trackList);
}
