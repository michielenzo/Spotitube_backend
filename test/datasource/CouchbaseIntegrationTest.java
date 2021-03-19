package datasource;

import datasource.datamapper.OwnerDataMapper;
import datasource.datamapper.PlayListDataMapper;
import datasource.datamapper.TrackDataMapper;
import org.junit.Before;
import org.junit.Test;

public class CouchbaseIntegrationTest {

    private OwnerDataMapper ownerDataMapper;
    private PlayListDataMapper playListDataMapper;
    private TrackDataMapper trackDataMapper;

    @Before
    public void initialize(){
        ownerDataMapper = new OwnerDataMapper(new DatabaseConnector(new ConfigReader()));
        playListDataMapper = new PlayListDataMapper(new DatabaseConnector(new ConfigReader()));
        trackDataMapper = new TrackDataMapper(new DatabaseConnector(new ConfigReader()));
    }

    @Test
    public void test(){

    }
}
