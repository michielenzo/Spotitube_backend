package service;

import domain.objects.Owner;
import domain.objects.PlayList;
import rest.dto.CreatePlayListRequest;

import javax.inject.Inject;
import java.util.List;

public class PlayListService implements IPlayListService{

    private IPlayListDataMapper playListDataMapper;

    private IOwnerDataMapper ownerDataMapper;

    @Inject
    public PlayListService(IPlayListDataMapper playListDataMapper, IOwnerDataMapper ownerDataMapper) {
        this.playListDataMapper = playListDataMapper;
        this.ownerDataMapper = ownerDataMapper;
    }

    @Override
    public List<PlayList> getAllPlayLists(String token) {
        return playListDataMapper.readAllFromUser(token);
    }

    @Override
    public void deletePlayList(int id) {
        playListDataMapper.delete(id);
    }

    @Override
    public void createPlayList(CreatePlayListRequest dto, String token) {
        Owner owner = ownerDataMapper.readByToken(token);
        playListDataMapper.create(dto.getName(), owner.getUsername());
    }

    @Override
    public void changeName(PlayList playList) {
        playListDataMapper.updateName(playList);
    }
}
