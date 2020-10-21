package service;

import domain.objects.PlayList;
import domain.objects.Song;
import domain.objects.Track;
import domain.objects.Video;
import rest.dto.AllPlayListsResponse;
import rest.dto.GetTrackListResponse;

import javax.enterprise.inject.Default;
import java.util.ArrayList;
import java.util.List;

@Default
public class DTOBuilderService implements IDTOBuilderService{

    @Override
    public AllPlayListsResponse buildAllPlayListsResponseDTO(List<PlayList> allPlayLists, String token) {
        AllPlayListsResponse dto = new AllPlayListsResponse();
        if(allPlayLists.size() > 0) {
            dto.setLength(calculateTotalAllPlayListLength(allPlayLists));
        }else {
            dto.setLength(0);
        }
        dto.setPlaylists(new ArrayList<>());
        if(allPlayLists.size() > 0) {
            for (domain.objects.PlayList playList : allPlayLists) {
                dto.getPlaylists().add(buildPlayListDTO(playList, token));
            }
        }
        return dto;
    }

    @Override
    public GetTrackListResponse buildGetAllTracksResponseDTO(List<Track> trackList) {
        GetTrackListResponse dto = new GetTrackListResponse();
        dto.setTracks(new ArrayList<>());
        if(trackList.size() > 0){
            for(domain.objects.Track track: trackList){
                dto.getTracks().add(buildTrackDTO(track));
            }
        }
        return dto;
    }

    private rest.dto.PlayList buildPlayListDTO(domain.objects.PlayList playList, String token) {
        rest.dto.PlayList playListDTO = new rest.dto.PlayList();
        playListDTO.setId(playList.getId());
        playListDTO.setName(playList.getName());
        if(token.equals(playList.getOwner().getToken())){
            playListDTO.setOwner(true);
        }else{
            playListDTO.setOwner(false);
        }
        playListDTO.setTracks(new ArrayList<>());
        for(Track track: playList.getTrackList()){
            playListDTO.getTracks().add(buildTrackDTO(track));
        }
        return playListDTO;
    }

    private rest.dto.Track buildTrackDTO(domain.objects.Track track) {
        rest.dto.Track trackDTO = new rest.dto.Track();
        trackDTO.setId(track.getId());
        trackDTO.setPerformer(track.getPerformer());
        trackDTO.setTitle(track.getTitle());
        trackDTO.setDuration(track.getDuration());
        trackDTO.setOfflineAvailable(track.isOfflineAvailable());
        trackDTO.setPlaycount(track.getPlayCount());
        if(track instanceof Song){
            trackDTO.setAlbum(((Song)track).getAlbum());
        }else{
            trackDTO.setPublicationDate(((Video)track).getPublicationDate());
            trackDTO.setDescription(((Video)track).getDescription());
        }
        return trackDTO;
    }

    private int calculateTotalAllPlayListLength(List<domain.objects.PlayList> allPlayLists) {
        int count = 0;
        for (domain.objects.PlayList playList: allPlayLists) {
            for (domain.objects.Track track: playList.getTrackList()) {
                count += track.getDuration();
            }
        }
        return count;
    }

}
