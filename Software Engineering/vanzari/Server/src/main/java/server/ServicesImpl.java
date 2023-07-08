package server;

import domain.*;
import persistence.*;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServicesImpl implements IServices {
    private ISalesAgent salesAgentRepo;

    private IProduct productRepo;

    private IClient clientRepo;

    private IOffer offerRepo;

    private IOrder orderRepo;

    private Map<String, IObserver> loggedClients;

    public ServicesImpl(ISalesAgent salesAgentRepo, IProduct productRepo, IClient clientRepo, IOffer offerRepo, IOrder orderRepo) {
        this.salesAgentRepo = salesAgentRepo;
        this.productRepo = productRepo;
        this.clientRepo = clientRepo;
        this.offerRepo = offerRepo;
        this.orderRepo = orderRepo;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    /*public ServicesImpl(ISalesAgent salesAgentRepo,) {
        this.salesAgentRepo = salesAgentRepo;
        this.loggedClients = new ConcurrentHashMap<>();
    }*/

    public SalesAgent findSalesAgent(String username, String password) throws SQLException {
        SalesAgent user = salesAgentRepo.findSalesAgent(username, password);

        if (user == null) throw new IllegalArgumentException("Nu exista user logat cu aceste date");

        return user;
    }

    public Client findClient(String username, String password) throws SQLException {
        Client user = clientRepo.findClient(username, password);

        if (user == null) throw new IllegalArgumentException("Nu exista user logat cu aceste date");

        return user;
    }

    public void loginSA(SalesAgent user, IObserver client) throws Exceptions {
        if (loggedClients.containsKey(user.getUsername())) {
            throw new Exceptions("User already logged in.");
        } else {
            try{
                this.findSalesAgent(user.getUsername(), user.getPassword());
                loggedClients.put(user.getUsername(), client);
                //return s;
            }catch (IllegalArgumentException | SQLException e){
                throw new Exceptions("Wrong data.");
            }

        }
    }

    public void logoutSA(SalesAgent user, IObserver client) throws Exceptions {
        IObserver removedClient = loggedClients.remove(user.getUsername());
        if (removedClient == null) {
            throw new Exceptions("User is not logged in.");
        }
    }

    public void loginCL(Client user, IObserver client) throws Exceptions {
        if (loggedClients.containsKey(user.getUsername())) {
            throw new Exceptions("User already logged in.");
        } else {
            try{
                this.findClient(user.getUsername(), user.getPassword());
                loggedClients.put(user.getUsername(), client);
                //return s;
            }catch (IllegalArgumentException | SQLException e){
                throw new Exceptions("Wrong data.");
            }

        }
    }

    public void logoutCL(Client user, IObserver client) throws Exceptions {
        IObserver removedClient = loggedClients.remove(user.getUsername());
        if (removedClient == null) {
            throw new Exceptions("User is not logged in.");
        }
    }

    public Iterable<Product> findAllProducts(){
        return productRepo.findAll();
    }

    public Iterable<Client> findAllClients(){
        return clientRepo.findAll();
    }

    public Iterable<Offer> findAllOffers(){
        return offerRepo.findAll();
    }

    public Iterable<Order> findAllOrders(){
        return orderRepo.findAll();
    }

    public void saveOffer(String product_ids, String agentUsername, String clientUsername){
        Offer offer = new Offer(product_ids,agentUsername,clientUsername);

        Offer o = offerRepo.save(offer);

        notifyOfferSent(o);

        //return o;
    }
    private void notifyOfferSent(Offer offer){
        for (IObserver observer : loggedClients.values()) {
            try {
                observer.offerSent(offer);
            } catch (Exceptions e) {
                System.err.println("Error notifying observer " + e);
            }
        }
    }
    @Override
    public void updateProduct(Product product, Integer newQuantity) throws Exceptions {
        product.setQuantity(newQuantity);

        productRepo.update(product);

        notifyUpdateProducts(product);
    }


    private void notifyUpdateProducts(Product product){
        for (IObserver observer : loggedClients.values()) {
            try {
                observer.updateProduct(product);
            } catch (Exceptions e) {
                System.err.println("Error notifying observer " + e);
            }
        }
    }


    public void saveOrder(String product_ids, String agentUsername, String clientUsername, String message){
        Order order = new Order(product_ids,agentUsername,clientUsername,message);

        Order o = orderRepo.save(order);

        // Search and delete the corresponding offer
        Offer offer = offerRepo.findOfferByCriteria(product_ids, agentUsername, clientUsername);
        if (offer != null) {
            offerRepo.delete(offer);
        }

        notifyOrderSent(o);
    }

    private void notifyOrderSent(Order order){
        for (IObserver observer : loggedClients.values()) {
            try {
                observer.orderSent(order);
            } catch (Exceptions e) {
                System.err.println("Error notifying observer " + e);
            }
        }
    }


    public Product findProduct(Integer id) throws Exceptions {
        Product product = productRepo.findOne(id);

        if (product == null) throw new IllegalArgumentException("Nu exista produs cu acest id");

        return product;
    }


}