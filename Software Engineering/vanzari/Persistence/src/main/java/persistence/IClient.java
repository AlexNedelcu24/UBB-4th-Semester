package persistence;

import domain.Client;
import domain.SalesAgent;

import java.sql.SQLException;

public interface IClient extends ICrudRepository<Integer, Client> {
    Client findClient(String username, String password) throws SQLException;
}
