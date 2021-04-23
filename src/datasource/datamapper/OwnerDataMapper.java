package datasource.datamapper;

import datasource.IDatabaseConnector;
import domain.objects.Owner;
import exceptions.DatabaseException;
import service.IOwnerDataMapper;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OwnerDataMapper implements IOwnerDataMapper{

    private IDatabaseConnector databaseConnector;

    @Inject
    public OwnerDataMapper(IDatabaseConnector databaseConnector){
        this.databaseConnector = databaseConnector;
    }

    public Owner read(String username){
        Owner owner = new Owner();

        try {
            String query = "select * from owner where username = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                owner.setUsername(resultSet.getString("username"));
                owner.setPassword(resultSet.getString("password"));
                if(resultSet.getString("token") != null){
                    owner.setToken(resultSet.getString("token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
        return owner;
    }

    public void update(String username, String password, String token){
        try {
            String query = "update owner set username = ?, password = ? , token = ? where username = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, token);
            statement.setString(4, username);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
    }

    public Owner readByToken(String token) {
        try {
            String query = "select * from owner where token = ?";
            PreparedStatement statement = databaseConnector.getConnection().prepareStatement(query);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                Owner owner = new Owner();
                owner.setUsername(resultSet.getString("username"));
                owner.setPassword(resultSet.getString("password"));
                owner.setToken(token);
                return owner;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseException();
        }
        return null;
    }
}
