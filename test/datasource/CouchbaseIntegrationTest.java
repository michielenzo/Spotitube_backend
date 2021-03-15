package datasource;

import datasource.datamapper.OwnerDataMapper;
import datasource.datamapper.PlayListDataMapper;
import domain.objects.Owner;
import domain.objects.PlayList;
import org.junit.Before;
import org.junit.Test;

public class CouchbaseIntegrationTest {

    private OwnerDataMapper ownerDataMapper;
    private PlayListDataMapper playListDataMapper;

    @Before
    public void initialize(){
        ownerDataMapper = new OwnerDataMapper(new DatabaseConnector(new ConfigReader()));
        playListDataMapper = new PlayListDataMapper(new DatabaseConnector(new ConfigReader()));
    }

    @Test
    public void test(){
        playListDataMapper.readAll("nekot");
    }
}
