package network.rpcprotocol;

import domain.*;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientRpcReflectionWorker implements Runnable, IObserver {

    private IServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientRpcReflectionWorker(IServices server, Socket connection){
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;/*
        String handlerName="handle"+(request).type();
        System.out.println("HandlerName "+handlerName);
        try {
            Method method=this.getClass().getDeclaredMethod(handlerName, Request.class);
            response=(Response)method.invoke(this,request);
            System.out.println("Method "+handlerName+ " invoked");
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }*/
        try {
            if (request.type() == RequestType.GET_CLIENTS) {
                System.out.println("Get cleints request ...");
                response = new Response.Builder().type(ResponseType.TAKE_CLIENTS).data(server.findAllClients()).build();
            }
            if (request.type() == RequestType.GET_OFFERS) {
                System.out.println("Get offers request ...");
                response = new Response.Builder().type(ResponseType.TAKE_OFFERS).data(server.findAllOffers()).build();
            }
            if (request.type() == RequestType.GET_ORDERS) {
                System.out.println("Get orders request ...");
                response = new Response.Builder().type(ResponseType.TAKE_ORDERS).data(server.findAllOrders()).build();
            }
            if (request.type() == RequestType.GET_PRODUCTS) {
                System.out.println("Get products request ...");
                response = new Response.Builder().type(ResponseType.TAKE_PRODUCTS).data(server.findAllProducts()).build();
            }
            if (request.type() == RequestType.GET_PRODUCT){
                System.out.println("Get product request ...");
                Integer id = (Integer) request.data();
                response = new Response.Builder().type(ResponseType.TAKE_PRODUCT).data(server.findProduct(id)).build();
            }
            if (request.type() == RequestType.SAVE_OFFER) {
                System.out.println("Save offer request ...");
                Offer offer = (Offer) request.data();
                server.saveOffer(offer.getProductIds(),offer.getSalesAgentUsername(),offer.getClientUsername());
                response = new Response.Builder().type(ResponseType.OK).build();
            }
            if (request.type() == RequestType.UPDATE_PRODUCT) {
                System.out.println("Update product request ...");
                Product product = (Product) request.data();
                server.updateProduct(product,product.getQuantity());
                response = new Response.Builder().type(ResponseType.OK).build();

            }
            if (request.type() == RequestType.SAVE_ORDER) {
                System.out.println("Save order request ...");
                Order order = (Order) request.data();
                server.saveOrder(order.getProductIds(),order.getSalesAgentUsername(),order.getClientUsername(),order.getMessage());
                response = new Response.Builder().type(ResponseType.OK).build();
            }
            if (request.type()== RequestType.LOGINSA){
                System.out.println("Login request ..."+request.type());
                SalesAgent user = (SalesAgent) request.data();
                try {
                    server.loginSA(user, this);
                    return okResponse;
                } catch (Exceptions e) {
                    connected=false;
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            if (request.type()== RequestType.LOGOUTSA){
                System.out.println("Logout request");
                // LogoutRequest logReq=(LogoutRequest)request;
                SalesAgent user = (SalesAgent) request.data();
                try {
                    server.logoutSA(user, this);
                    connected=false;
                    return okResponse;

                } catch (Exceptions e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            if (request.type()== RequestType.LOGINCL){
                System.out.println("Login request ..."+request.type());
                Client user = (Client) request.data();
                try {
                    server.loginCL(user, this);
                    return okResponse;
                } catch (Exceptions e) {
                    connected=false;
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }
            if (request.type()== RequestType.LOGOUTCL){
                System.out.println("Logout request");
                // LogoutRequest logReq=(LogoutRequest)request;
                Client user = (Client) request.data();
                try {
                    server.logoutCL(user, this);
                    connected=false;
                    return okResponse;

                } catch (Exceptions e) {
                    return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
                }
            }

        }catch (Exception e) {
            connected = false;
            return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
        }

        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    public void offerSent(Offer offer) throws Exceptions {
        Response resp = new Response.Builder().type(ResponseType.NEW_OFFER).data(offer).build();
        System.out.println("Offer received  " + offer);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new Exceptions("Sending error: " + e);
        }
    }

    public void orderSent(Order order) throws Exceptions {
        Response resp = new Response.Builder().type(ResponseType.NEW_ORDER).data(order).build();
        System.out.println("Order received  " + order);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new Exceptions("Sending error: " + e);
        }
    }

    public void updateProduct(Product product) throws Exceptions {
        Response resp = new Response.Builder().type(ResponseType.UPDATED_PRODUCT).data(product).build();
        System.out.println("Product updated  " + product);
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new Exceptions("Sending error: " + e);
        }
    }


}
