package service;

import domain.objects.PlayList;
import domain.objects.Track;
import rest.dto.ChangePlayListNameRequest;

public interface IDTOToDomainService {
    Track convertTrackDTOToTrack(rest.dto.Track track);
    PlayList convertPlayListDTOToPlayList(ChangePlayListNameRequest dto);
}
