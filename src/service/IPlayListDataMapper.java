package service;

import domain.objects.PlayList;

import java.util.List;

public interface IPlayListDataMapper {

    void create(String name, String username);
    void updateName(PlayList playList);
    void delete(int id);
    List<PlayList> readAllFromUser(String token);

}
