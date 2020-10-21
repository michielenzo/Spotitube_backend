package datasource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigReader {

    public String read(String property){
        String configPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                .getResource("database.properties")).getPath();

        Properties databaseProps = new Properties();

        try { databaseProps.load(new FileInputStream(configPath)); }
        catch (IOException e) { e.printStackTrace(); }

        return databaseProps.getProperty(property);
    }
}
