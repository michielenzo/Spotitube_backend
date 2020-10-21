package service;

import domain.objects.PlayList;
import rest.dto.CreatePlayListRequest;

import java.util.List;

public interface IPlayListService {
    List<PlayList> getAllPlayLists();
    void deletePlayList(int id);
    void createPlayList(CreatePlayListRequest dto, String token);
    void changeName(PlayList playList);
}
