package datasource;

import datasource.datamapper.OwnerDataMapper;
import domain.objects.Owner;
import org.junit.Before;
import org.junit.Test;

public class CouchbaseIntegrationTest {

    private OwnerDataMapper ownerDataMapper;

    @Before
    public void initialize(){
        ownerDataMapper = new OwnerDataMapper(new DatabaseConnector(new ConfigReader()));
    }

    @Test
    public void test(){
        ownerDataMapper.update("kungfuzemmel", "kutzooi", "nekot");
    }
}
