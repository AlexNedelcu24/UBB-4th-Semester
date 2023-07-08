package com.example.gui;

import domain.Offer;
import domain.Order;
import domain.Product;
import domain.SalesAgent;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import services.Exceptions;
import services.IObserver;
import services.IServices;

import java.io.IOException;

public class Agent2 implements IObserver {

    @FXML
    private Button logoutButton;

    private IServices server;

    private SalesAgent crtSalesAgent;

    Parent mainChatParent;

    Parent orderParent;



    private SendOfferWindow sendOfferController;
    private MakeOrderWindow makeOrderController;

    public Agent2(){};

    public Agent2(IServices server){
        this.server = server;
        System.out.println("constructor Agent2 cu server param");

    };

    public void setServer(IServices s) {
        server = s;
    }

    public void setParent(Parent p){
        mainChatParent=p;
    }

    public void setParentOrder(Parent p){ orderParent=p;}


    public void setSendOfferWindow(SendOfferWindow sendOfferController){ this.sendOfferController = sendOfferController;}
    public void setMakeOrderWindow(MakeOrderWindow makeOrderController){ this.makeOrderController = makeOrderController;}

    @FXML
    public void onUserLogout(ActionEvent event) throws IOException {
        logout();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    void logout() {
        try {
            server.logoutSA(crtSalesAgent, this);
        } catch (Exceptions e) {
            System.out.println("Logout error " + e);
        }

    }

    public void setUser(SalesAgent user) {
        this.crtSalesAgent = user;
    }


    public void onSendOffers(ActionEvent actionEvent) throws IOException, Exceptions {
        Stage stage=new Stage();
        stage.setTitle("Send offers Window for " +crtSalesAgent.getUsername());
        stage.setScene(new Scene(mainChatParent));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //sendOfferController.close();
                Platform.runLater(() -> {
                    stage.close();
                    System.exit(0);
                });
            }
        });

        stage.show();
        sendOfferController.setUser(crtSalesAgent);
        sendOfferController.loadClients();
        sendOfferController.loadProducts();
        //((Node)(actionEvent.getSource())).getScene().getWindow().hide();

    }


    public void onMakeOrders(ActionEvent event) throws IOException, Exceptions {
        Stage stage=new Stage();
        stage.setTitle("Make orders Window for " +crtSalesAgent.getUsername());
        stage.setScene(new Scene(orderParent));

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //sendOfferController.close();
                Platform.runLater(() -> {
                    stage.close();
                    System.exit(0);
                });
            }
        });

        stage.show();
        makeOrderController.setUser(crtSalesAgent);
        makeOrderController.loadOrders();
        makeOrderController.loadProducts();

    }

    @Override
    public void offerSent(Offer offer) throws Exceptions {

    }

    @Override
    public void orderSent(Order order) throws Exceptions {

    }

    @Override
    public void updateProduct(Product product) throws Exceptions {

    }


}
