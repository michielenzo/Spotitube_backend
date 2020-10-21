package datasource.datamapper;

import datasource.IDatabaseConnector;
import domain.objects.Owner;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OwnerDataMapper {

    private IDatabaseConnector databaseConnector;

    @Inject
    public OwnerDataMapper(IDatabaseConnector databaseConnector){
        this.databaseConnector = databaseConnector;
    }

    public void create(String userName, String password){
        try {
            Statement stmt = databaseConnector.getConnection().createStatement();
            String query = String.format("insert into owner values ('%s','%s')",userName ,password);
            stmt.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Owner read(String username){
        Owner owner = new Owner();
        try {
            Statement stmt = databaseConnector.getConnection().createStatement();
            String query = String.format("select * from owner where username = '%s'", username);
            ResultSet resultSet = stmt.executeQuery(query);
            if(resultSet.next()){
                owner.setUsername(resultSet.getString("username"));
                owner.setPassword(resultSet.getString("password"));
                if(resultSet.getString("token") != null){
                    owner.setToken(resultSet.getString("token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner;
    }

    public void update(String username, String password, String token){
        try {
            Statement stmt = databaseConnector.getConnection().createStatement();
            String query = String.format(
                    "update owner set username = '%s', password = '%s' , token = '%s' where username = '%s'",
                    username,
                    password,
                    token,
                    username);
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String username){
        try {
            Statement stmt = databaseConnector.getConnection().createStatement();
            String query = String.format("delete from owner where username = '%s'", username);
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Owner readByToken(String token) {
        try {
            Statement stmt = databaseConnector.getConnection().createStatement();
            String query = String.format("select * from owner where token = '%s'", token);
            ResultSet resultSet = stmt.executeQuery(query);
            if(resultSet.next()){
                Owner owner = new Owner();
                owner.setUsername(resultSet.getString("username"));
                owner.setPassword(resultSet.getString("password"));
                owner.setToken(token);
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
