package persistence;

import domain.Offer;

public interface IOffer extends ICrudRepository<Integer, Offer> {

    Offer findOfferByCriteria(String product_ids, String sales_agent_username, String client_username);
}
