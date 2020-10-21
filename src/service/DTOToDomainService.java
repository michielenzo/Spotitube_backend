package service;

import domain.objects.PlayList;
import domain.objects.Song;
import domain.objects.Track;
import domain.objects.Video;
import rest.dto.ChangePlayListNameRequest;

public class DTOToDomainService implements IDTOToDomainService {

    @Override
    public Track convertTrackDTOToTrack(rest.dto.Track trackDTO) {
        Track track;
        if(isSong(trackDTO)){
            track = new Song();
            ((Song) track).setAlbum(trackDTO.getAlbum());
        }else{
            track = new Video();
            ((Video) track).setPublicationDate(trackDTO.getPublicationDate());
            ((Video) track).setDescription(trackDTO.getDescription());
        }
        track.setDuration(trackDTO.getDuration());
        track.setId(trackDTO.getId());
        track.setOfflineAvailable(trackDTO.isOfflineAvailable());
        track.setPerformer(trackDTO.getPerformer());
        track.setTitle(trackDTO.getTitle());
        track.setPlayCount(trackDTO.getPlaycount());
        return track;
    }

    @Override
    public PlayList convertPlayListDTOToPlayList(ChangePlayListNameRequest dto){
        PlayList playList = new PlayList();
        playList.setId(dto.getId());
        playList.setName(dto.getName());
        return playList;
    }

    private boolean isSong(rest.dto.Track track) {
        return track.getAlbum() != null;
    }
}
