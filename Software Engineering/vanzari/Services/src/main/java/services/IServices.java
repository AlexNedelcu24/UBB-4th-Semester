package services;

import domain.*;

public interface IServices {

    void loginSA(SalesAgent salesAgent, IObserver observer) throws Exceptions;

    void logoutSA(SalesAgent salesAgent, IObserver client) throws Exceptions;

    void loginCL(Client c, IObserver observer) throws Exceptions;

    void logoutCL(Client c, IObserver observer) throws Exceptions;

    Product findProduct(Integer id) throws Exceptions;

    Iterable<Product> findAllProducts() throws Exceptions;

    Iterable<Offer> findAllOffers() throws Exceptions;

    Iterable<Order> findAllOrders() throws Exceptions;

    void saveOffer(String product_ids, String agentUsername , String clientUsername) throws Exceptions;

    void updateProduct(Product product, Integer newQuantity) throws Exceptions;

    void saveOrder(String product_ids, String agentUsername , String clientUsername, String message) throws Exceptions;

    Iterable<Client> findAllClients() throws Exceptions;

}
