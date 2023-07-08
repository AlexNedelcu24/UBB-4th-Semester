package persistence;

import domain.SalesAgent;

import java.sql.SQLException;

public interface ISalesAgent extends ICrudRepository<Integer, SalesAgent> {

    SalesAgent findSalesAgent(String username, String password) throws SQLException;

}

