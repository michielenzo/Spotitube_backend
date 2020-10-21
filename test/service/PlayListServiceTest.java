package service;

import datasource.datamapper.OwnerDataMapper;
import datasource.datamapper.PlayListDataMapper;
import domain.objects.Owner;
import domain.objects.PlayList;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import rest.dto.CreatePlayListRequest;

public class PlayListServiceTest {

    private PlayListService playListService;

    @Rule public MockitoRule rule = MockitoJUnit.rule();
    @Mock private PlayListDataMapper playListDataMapper;
    @Mock private OwnerDataMapper ownerDataMapper;

    @Before
    public void initialize(){
        playListService = new PlayListService(playListDataMapper, ownerDataMapper);
    }

    @Test
    public void test_getAllPlayLists_method(){
        playListService.getAllPlayLists();
        Mockito.verify(playListDataMapper).readAll();
    }

    @Test
    public void test_deletePlayList_method(){
        playListService.deletePlayList(1);
        Mockito.verify(playListDataMapper).delete(1);
    }

    @Test
    public void test_createPlayList_method(){
        CreatePlayListRequest dto = new CreatePlayListRequest();
        dto.setId(1);
        dto.setName("Henk");
        dto.setOwner(true);

        Owner owner = new Owner();
        owner.setUsername("Henk123");

        Mockito.when(ownerDataMapper.readByToken("1234")).thenReturn(owner);

        playListService.createPlayList(dto, "1234");

        Mockito.verify(playListDataMapper).create(dto.getName(), owner.getUsername());
    }

    @Test
    public void test_changeName_method(){
        PlayList playList = new PlayList();
        playListService.changeName(playList);
        Mockito.verify(playListDataMapper).update(playList);
    }

}
