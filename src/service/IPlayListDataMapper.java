package service;

import domain.objects.PlayList;

import java.util.List;

public interface IPlayListDataMapper {

    void create(String name, String username);
    void update(PlayList playList);
    void delete(int id);
    List<PlayList> readAll();

}
