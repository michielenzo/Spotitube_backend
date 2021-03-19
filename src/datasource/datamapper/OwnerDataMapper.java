package datasource.datamapper;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryResult;
import datasource.IDatabaseConnector;
import domain.objects.Owner;
import service.IOwnerDataMapper;

import javax.inject.Inject;

public class OwnerDataMapper implements IOwnerDataMapper{

    private IDatabaseConnector databaseConnector;

    @Inject
    public OwnerDataMapper(IDatabaseConnector databaseConnector){
        this.databaseConnector = databaseConnector;
    }

    public Owner read(String username){
        Cluster cluster = databaseConnector.getConnection();

        QueryResult result = cluster.query(
                "SELECT * FROM spotitube.main.`User` WHERE username=?",
                QueryOptions.queryOptions().parameters(JsonArray.from(username)));
        JsonObject data = (JsonObject) result.rowsAsObject().get(0).get("User");

        Owner owner = new Owner();
        owner.setUsername(username);
        owner.setPassword(data.getString("password"));
        owner.setToken(data.getString("token"));

        return owner;
    }

    public void update(String username, String password, String token){
        Cluster cluster = databaseConnector.getConnection();
        cluster.query(
                "UPDATE spotitube.main.`User` SET username =?, `password` =?, token =? WHERE username =?",
                QueryOptions.queryOptions().parameters(JsonArray.from(username, password, token, username)));
    }

    public Owner readByToken(String token) {
        Cluster cluster = databaseConnector.getConnection();

        QueryResult result = cluster.query(
                "SELECT * FROM spotitube.main.`User` WHERE token=?",
                QueryOptions.queryOptions().parameters(JsonArray.from(token)));
        JsonObject data = (JsonObject) result.rowsAsObject().get(0).get("User");

        Owner owner = new Owner();
        owner.setToken(token);
        owner.setPassword(data.getString("password"));
        owner.setUsername(data.getString("username"));

        return owner;
    }
}
