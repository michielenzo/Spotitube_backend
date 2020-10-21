package datasource;

import org.junit.Before;
import org.junit.Test;

public class ConfigReaderTest {

    private ConfigReader configReader;

    @Before
    public void initialize(){
        configReader = new ConfigReader();
    }

    @Test
    public void test(){
        System.out.println(configReader.read("driver"));
    }
}
