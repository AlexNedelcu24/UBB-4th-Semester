package network.rpcprotocol;

import domain.*;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServiceRpcProxy implements IServices {

    private String host;
    private int port;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServiceRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
    }

    public void loginSA(SalesAgent user, IObserver client) throws Exceptions {
        initializeConnection();

        Request req = new Request.Builder().type(RequestType.LOGINSA).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
    }

    public void logoutSA(SalesAgent user, IObserver client) throws Exceptions {

        Request req = new Request.Builder().type(RequestType.LOGOUTSA).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exceptions(err);
        }
    }

    public void loginCL(Client user, IObserver client) throws Exceptions {
        initializeConnection();

        Request req = new Request.Builder().type(RequestType.LOGINCL).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
    }

    public void logoutCL(Client user, IObserver client) throws Exceptions {

        Request req = new Request.Builder().type(RequestType.LOGOUTCL).data(user).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new Exceptions(err);
        }
    }

    @Override
    public Iterable<Client> findAllClients() throws Exceptions {
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.GET_CLIENTS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        return (List<Client>) response.data();
    }

    @Override
    public Iterable<Product> findAllProducts() throws Exceptions {
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.GET_PRODUCTS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        return (List<Product>) response.data();
    }

    @Override
    public Iterable<Offer> findAllOffers() throws Exceptions {
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.GET_OFFERS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        return (List<Offer>) response.data();
    }

    @Override
    public Iterable<Order> findAllOrders() throws Exceptions {
        //initializeConnection();
        Request req = new Request.Builder().type(RequestType.GET_ORDERS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        return (List<Order>) response.data();
    }

    @Override
    public Product findProduct(Integer id) throws Exceptions {
        Request req = new Request.Builder().type(RequestType.GET_PRODUCT).data(id).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        return (Product) response.data();
    }


    @Override
    public void saveOffer(String product_ids, String agentUsername, String clientUsername) throws Exceptions {
        Offer offer = new Offer(product_ids,agentUsername,clientUsername);

        Request req = new Request.Builder().type(RequestType.SAVE_OFFER).data(offer).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        //return (Competitor) response.data();

    }

    @Override
    public void updateProduct(Product product, Integer newQuantity) throws Exceptions {
        product.setQuantity(newQuantity);

        Request req = new Request.Builder().type(RequestType.UPDATE_PRODUCT).data(product).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        //return (Competitor) response.data();

    }

    @Override
    public void saveOrder(String product_ids, String agentUsername, String clientUsername, String message) throws Exceptions {
        Order order = new Order(product_ids,agentUsername,clientUsername,message);

        Request req = new Request.Builder().type(RequestType.SAVE_ORDER).data(order).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new Exceptions(err);
        }
        //return (Competitor) response.data();

    }

    private void sendRequest(Request request) throws Exceptions {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Exceptions("Error sending object " + e);
        }

    }

    private Response readResponse() {
        Response response = null;
        try {
            response = qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleUpdate(Response response){

        if (response.type()== ResponseType.NEW_OFFER){

            Offer offer = (Offer) response.data();
            try {
                client.offerSent(offer);
            } catch (Exceptions e) {
                e.printStackTrace();
            }
        }

        if (response.type()== ResponseType.NEW_ORDER){

            Order order = (Order) response.data();
            try {
                client.orderSent(order);
            } catch (Exceptions e) {
                e.printStackTrace();
            }
        }

        if (response.type()== ResponseType.UPDATED_PRODUCT){

            Product product = (Product) response.data();
            try {
                client.updateProduct(product);
            } catch (Exceptions e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.NEW_OFFER || response.type() == ResponseType.NEW_ORDER || response.type() == ResponseType.UPDATED_PRODUCT;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
